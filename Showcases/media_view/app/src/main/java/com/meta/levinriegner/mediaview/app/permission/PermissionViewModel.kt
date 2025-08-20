// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.permission

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class PermissionViewModel @Inject constructor() : ViewModel() {

  private val _state = MutableStateFlow<PermissionState>(PermissionState.CheckPermissionState)
  val state = _state.asStateFlow()
  
  private var isProcessingPermissionResult = false
  
  companion object {
    private const val FLAG_RESET_DELAY = 1000L // 1 second
  }

  private fun resetProcessingFlag() {
    // Reset the flag after a delay to allow future permission checks
    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
      kotlinx.coroutines.delay(FLAG_RESET_DELAY)
      isProcessingPermissionResult = false
      Timber.d("Reset isProcessingPermissionResult flag")
    }
  }

  fun onCheckPermissionResult(granted: Boolean) {
    if (isProcessingPermissionResult) {
      Timber.d("Already processing permission result, skipping")
      return
    }
    
    isProcessingPermissionResult = true
    Timber.i("Storage permission status granted: $granted")
    if (granted) {
      _state.value = PermissionState.PermissionAccepted
    } else {
      _state.value = PermissionState.RequestPermission
    }
    resetProcessingFlag()
  }

  fun onStoragePermissionGranted() {
    if (isProcessingPermissionResult) {
      Timber.d("Already processing permission result, skipping")
      return
    }
    
    isProcessingPermissionResult = true
    Timber.i("Storage permission granted, transitioning to PermissionAccepted")
    _state.value = PermissionState.PermissionAccepted
    resetProcessingFlag()
  }

  fun onStoragePermissionDenied() {
    if (isProcessingPermissionResult) {
      Timber.d("Already processing permission result, skipping")
      return
    }
    
    isProcessingPermissionResult = true
    Timber.i("Storage permission denied, transitioning to PermissionDenied")
    _state.value = PermissionState.PermissionDenied
    resetProcessingFlag()
  }
}
