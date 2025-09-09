// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.player.menu.minimized

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meta.levinriegner.mediaview.app.events.AppEvent
import com.meta.levinriegner.mediaview.app.events.AppEventListener
import com.meta.levinriegner.mediaview.app.events.EventBus
import com.meta.levinriegner.mediaview.app.events.MediaPlayerEvent
import com.meta.levinriegner.mediaview.app.panel.PanelDelegate
import com.meta.levinriegner.mediaview.data.gallery.model.MediaModel
import com.meta.levinriegner.mediaview.data.gallery.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SpatialDeleteConfirmationViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val panelDelegate: PanelDelegate,
    private val eventBus: EventBus,
    private val galleryRepository: GalleryRepository,
) : ViewModel(), AppEventListener {

  private var mediaModel: MediaModel? = null
  private var isDeleting = false

  init {
    eventBus.register(this)
  }

  override fun onCleared() {
    eventBus.unregister(this)
    super.onCleared()
  }

  override fun onEvent(event: AppEvent) {
    when (event) {
      is MediaPlayerEvent.ShowDeleteConfirmation -> {
        mediaModel = event.mediaModel
      }
    }
  }

  fun confirmDelete() {
    val model = mediaModel ?: return

    if (isDeleting) {
      return
    }

    isDeleting = true

    // Hide the dialog immediately for responsiveness
    panelDelegate.hideSpatialDeleteConfirmation()

    viewModelScope.launch {
      try {
        galleryRepository.deleteMedia(model.id)
        eventBus.post(MediaPlayerEvent.Deleted(model.id))
        panelDelegate.closeMediaPanel(model)
        eventBus.post(MediaPlayerEvent.Close(model.id))
      } catch (e: Exception) {
        Timber.e("Error during deletion process: ${e.message}")
      } finally {
        isDeleting = false
      }
    }
  }

  fun cancelDelete() {
    panelDelegate.hideSpatialDeleteConfirmation()
  }
}
