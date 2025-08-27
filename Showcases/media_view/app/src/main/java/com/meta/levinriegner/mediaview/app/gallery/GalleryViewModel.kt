// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meta.levinriegner.mediaview.app.events.AppEvent
import com.meta.levinriegner.mediaview.app.events.AppEventListener
import com.meta.levinriegner.mediaview.app.events.EditEvent
import com.meta.levinriegner.mediaview.app.events.EventBus
import com.meta.levinriegner.mediaview.app.events.FilterAppEvent
import com.meta.levinriegner.mediaview.app.events.MediaPlayerEvent
import com.meta.levinriegner.mediaview.app.events.NavigationEvent
import com.meta.levinriegner.mediaview.app.events.UploadAppEvent
import com.meta.levinriegner.mediaview.app.panel.PanelDelegate
import com.meta.levinriegner.mediaview.app.shared.model.UiState
import com.meta.levinriegner.mediaview.data.gallery.model.MediaFilter
import com.meta.levinriegner.mediaview.data.gallery.model.MediaModel
import com.meta.levinriegner.mediaview.data.gallery.model.MediaSortBy
import com.meta.levinriegner.mediaview.data.gallery.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class GalleryViewModel
@Inject
constructor(
    private val galleryRepository: GalleryRepository,
    private val panelDelegate: PanelDelegate,
    private val eventBus: EventBus,
) : ViewModel(), AppEventListener {

  private val _state = MutableStateFlow<UiState<List<MediaModel>>>(UiState.Idle)
  val state = _state.asStateFlow()

  private val _events = MutableSharedFlow<GalleryEvent>()
  val events = _events.asSharedFlow()

  private val _filter = MutableStateFlow(MediaFilter.ALL)
  val filter = _filter.asStateFlow()

  private val _sortBy = MutableStateFlow(MediaSortBy.DateDesc)
  val sortBy = _sortBy.asStateFlow()

  private val _showMetadata = MutableStateFlow(false)
  val showMetadata = _showMetadata.asStateFlow()

  private val _isSelectionMode = MutableStateFlow(false)
  val isSelectionMode = _isSelectionMode.asStateFlow()

  private val _selectedItems = MutableStateFlow<Set<Long>>(emptySet())
  val selectedItems = _selectedItems.asStateFlow()

  private val _showDeleteConfirmation = MutableStateFlow(false)
  val showDeleteConfirmation = _showDeleteConfirmation.asStateFlow()

  init {
    // Will trigger the initial load
    subscribeToFilterChanges()
    eventBus.register(this)
  }

  fun onOnboardingButtonPressed() {
    panelDelegate.toggleOnboarding(true)
  }

  fun onMediaSelected(mediaModel: MediaModel) {
    Timber.i("Opening media: ${mediaModel.debugPrint()}")
    Timber.d("With name: ${mediaModel.name}")
    panelDelegate.openMediaPanel(mediaModel)
  }

  fun onSortBy(sortBy: MediaSortBy) {
    Timber.i("Sorting by: $sortBy")
    _sortBy.value = sortBy
    loadMedia()
  }

  fun onToggleMetadata(show: Boolean) {
    Timber.i("Toggling metadata to $show")
    _showMetadata.value = show
  }

  fun onToggleSelectionMode() {
    Timber.i("Toggling selection mode")
    val newSelectionMode = !_isSelectionMode.value
    _isSelectionMode.value = newSelectionMode
    if (!newSelectionMode) {
      // Clear selection when exiting selection mode
      _selectedItems.value = emptySet()
    }
    // Notify other components about selection mode change
    eventBus.post(FilterAppEvent.SelectionModeChanged(newSelectionMode))
  }

  fun onItemSelectionToggled(itemId: Long) {
    Timber.i("Toggling selection for item: $itemId")
    val currentSelected = _selectedItems.value.toMutableSet()
    if (currentSelected.contains(itemId)) {
      currentSelected.remove(itemId)
    } else {
      currentSelected.add(itemId)
    }
    _selectedItems.value = currentSelected
  }

  fun onDeleteSelected() {
    Timber.i("Showing delete confirmation for ${_selectedItems.value.size} selected items")
    _showDeleteConfirmation.value = true
  }

  fun onDeleteConfirmed() {
    Timber.i("Deleting ${_selectedItems.value.size} selected items")
    val itemsToDelete = _selectedItems.value.toList()
    
    _showDeleteConfirmation.value = false
    
    viewModelScope.launch {
      try {
        // Delete selected items from repository with delays to prevent race conditions
        itemsToDelete.forEachIndexed { index, itemId ->
          if (index > 0) {
            kotlinx.coroutines.delay(100) // 100ms delay between deletions
          }
          Timber.i("Deleting item ${index + 1}/${itemsToDelete.size}: ID=$itemId")
          galleryRepository.deleteMedia(itemId)
        }
        
        // Clear selection and exit selection mode
        _selectedItems.value = emptySet()
        _isSelectionMode.value = false
        
        // Notify other components about selection mode change
        eventBus.post(FilterAppEvent.SelectionModeChanged(false))
        
        // Reload media to reflect changes
        loadMedia()
        
        Timber.i("Successfully deleted ${itemsToDelete.size} items")
      } catch (t: Throwable) {
        Timber.e("Failed to delete selected items: ${t.message}")
        // TODO: Show error message to user
      }
    }
  }

  fun onDeleteCancelled() {
    Timber.i("Delete operation cancelled")
    _showDeleteConfirmation.value = false
  }



  fun loadMedia(
      filter: MediaFilter = this.filter.value,
      sortBy: MediaSortBy = _sortBy.value,
  ) =
      viewModelScope.launch {
        _state.value = UiState.Loading
        try {
          Timber.i("Getting media for filter: $filter and sort by: $sortBy")
          val media = galleryRepository.getMedia(filter, sortBy)
          Timber.i("Got media: ${media.size}")
          _state.value = UiState.Success(media)
          // DEV: Use to navigate while developing
          //                val devModel = media[0]
          //                onMediaSelected(devModel)
          //                delay(200L)
          //                panelDelegate.maximizeMedia(devModel)
        } catch (t: Throwable) {
          Timber.w("Failed to get media: ${t.message}")
          _state.value = UiState.Error("Failed to get media: ${t.message}")
        }
      }

  private fun subscribeToFilterChanges() =
      viewModelScope.launch { filter.collect { loadMedia(it) } }

  override fun onEvent(event: AppEvent) {
    when (event) {
      is UploadAppEvent.UploadSuccess -> loadMedia()
      is UploadAppEvent.UploadFailed -> {
        viewModelScope.launch { _events.emit(GalleryEvent.UploadFailed(event.error)) }
      }

      is FilterAppEvent.FilterChanged -> _filter.value = event.filter

      is MediaPlayerEvent.Deleted -> {
        if (state.value is UiState.Success) {
          val media = (state.value as UiState.Success<List<MediaModel>>).data
          val updatedMedia = media.filter { it.id != event.mediaId }
          _state.value = UiState.Success(updatedMedia)
        }
      }

      is NavigationEvent.PrivacyPolicyAccepted -> {
        panelDelegate.toggleGallery(true)
      }

      is EditEvent.SaveImageCompleted -> {
        if (event.success) {
          loadMedia()
        }
      }
    }
  }

  override fun onCleared() {
    eventBus.unregister(this)
    super.onCleared()
  }
}
