// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.player.menu.minimized

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.meta.levinriegner.mediaview.app.shared.theme.MediaViewTheme
import com.meta.levinriegner.mediaview.app.events.EventBus
import com.meta.levinriegner.mediaview.app.events.MediaPlayerEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MinimizedMenuActivity : ComponentActivity() {

  private val viewModel by viewModels<MinimizedMenuViewModel>()
  
  @Inject
  lateinit var eventBus: EventBus

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    buildUi()
  }

  private fun buildUi() {
    setContent {
      MediaViewTheme {
        MinimizedMenuView(
            onMaximize = { viewModel.maximize() },
            onClose = {
              viewModel.close()
              finish()
            },
            onDelete = {
              viewModel.showSpatialDeleteConfirmation()
            },
        )
      }
    }
  }
}
