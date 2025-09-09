// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.player.menu.minimized

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.meta.levinriegner.mediaview.app.shared.theme.MediaViewTheme
import com.meta.levinriegner.mediaview.app.shared.view.component.DeleteConfirmationDialog
import com.meta.levinriegner.mediaview.data.gallery.model.MediaModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpatialDeleteConfirmationActivity : ComponentActivity() {

  private val viewModel by viewModels<SpatialDeleteConfirmationViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    buildUi()
  }

  private fun buildUi() {
    setContent {
      MediaViewTheme {
        DeleteConfirmationDialog(
            title = "Are you sure you want to delete this media file?",
            description = "This file will be deleted immediately.\nYou can't undo this action.",
            deleteText = "Delete file",
            cancelText = "Cancel",
            onDelete = {
              viewModel.confirmDelete()
            },
            onCancel = {
              viewModel.cancelDelete()
            },
            onDismiss = {
              viewModel.cancelDelete()
            }
        )
      }
    }
  }
}
