// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.gallery.filter

import androidx.lifecycle.ViewModel
import com.meta.levinriegner.mediaview.app.events.AppEventListener
import com.meta.levinriegner.mediaview.app.events.EventBus
import com.meta.levinriegner.mediaview.app.events.FilterAppEvent
import com.meta.levinriegner.mediaview.app.panel.PanelDelegate
import com.meta.levinriegner.mediaview.data.gallery.model.MediaFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

@HiltViewModel
class MediaFilterViewModel
@Inject
constructor(
    private val panelDelegate: PanelDelegate,
    private val eventBus: EventBus,
) : ViewModel(), AppEventListener {

  private val _filters =
      MutableStateFlow(
          MediaFilter.entries.map { filter ->
            UiMediaFilter(filter, filter == MediaFilter.initial)
          })
  val filters = _filters.asStateFlow()

  private val _isSelectionMode = MutableStateFlow(false)
  val isSelectionMode = _isSelectionMode.asStateFlow()

  fun onFilterSelected(filter: UiMediaFilter) {
    Timber.i("On Filter Selected: ${filter.type}")
    // Prevent filter changes when in selection mode
    if (_isSelectionMode.value) {
      Timber.i("Filter change blocked - currently in selection mode")
      return
    }
    eventBus.post(FilterAppEvent.FilterChanged(filter.type))
    _filters.value = _filters.value.map { it.copy(isSelected = it.type == filter.type) }
  }

  fun onUpload() {
    Timber.i("On Upload")
    // Prevent uploads when in selection mode
    if (_isSelectionMode.value) {
      Timber.i("Upload blocked - currently in selection mode")
      return
    }
    panelDelegate.openUploadPanel()
  }

  init {
    eventBus.register(this)
  }

  override fun onEvent(event: com.meta.levinriegner.mediaview.app.events.AppEvent) {
    when (event) {
      is FilterAppEvent.SelectionModeChanged -> {
        Timber.i("Selection mode changed: ${event.isSelectionMode}")
        _isSelectionMode.value = event.isSelectionMode
      }
      is FilterAppEvent.ResetToAllFilter -> {
        Timber.i("Resetting to ALL filter")
        eventBus.post(FilterAppEvent.FilterChanged(MediaFilter.ALL))
        _filters.value = _filters.value.map { it.copy(isSelected = it.type == MediaFilter.ALL) }
      }
    }
  }

  override fun onCleared() {
    eventBus.unregister(this)
    super.onCleared()
  }
}
