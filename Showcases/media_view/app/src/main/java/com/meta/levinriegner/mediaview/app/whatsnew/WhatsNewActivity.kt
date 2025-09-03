// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.whatsnew

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.meta.levinriegner.mediaview.BuildConfig
import com.meta.levinriegner.mediaview.R
import com.meta.levinriegner.mediaview.app.shared.Constants
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.levinriegner.mediaview.app.shared.theme.MediaViewTheme
import com.meta.levinriegner.mediaview.app.shared.view.component.CloseButton
import com.meta.levinriegner.mediaview.app.shared.view.component.RoundedButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WhatsNewActivity : ComponentActivity() {
  private val viewModel: WhatsNewViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    buildUi()
  }

  private fun buildUi() {
    setContent {
      // UI
      MediaViewTheme {
        Surface(
            modifier =
                Modifier.fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = AppColor.MetaBlu,
                        shape = RoundedCornerShape(Dimens.radiusMedium))
                    .clip(shape = RoundedCornerShape(Dimens.radiusMedium))) {
              val uriHandler = LocalUriHandler.current

              val whatsNew = viewModel.releaseNotes.collectAsState().value

              Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier =
                        Modifier.fillMaxWidth(fraction = .30f)
                            .fillMaxHeight()
                            .background(AppColor.DarkBackgroundSweep)
                            .padding(
                                horizontal = Dimens.small,
                            )) {
                      Column(
                          horizontalAlignment = Alignment.CenterHorizontally,
                          verticalArrangement = Arrangement.Center,
                          modifier = Modifier.fillMaxSize()) {
                            Image(
                                rememberAsyncImagePainter(R.drawable.logo),
                                "logo",
                            )
                          }
                    }

                Column(
                    modifier =
                        Modifier.fillMaxSize()
                            .background(AppColor.BackgroundSweep)
                            .padding(Dimens.small)) {

                      // Top bar with close button
                      Row(
                          modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                          horizontalArrangement = Arrangement.SpaceBetween,
                          verticalAlignment = Alignment.Top) {
                            Text(
                                stringResource(R.string.whats_new_title, BuildConfig.VERSION_NAME),
                                color = AppColor.White,
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.titleMedium)
                            CloseButton(onPressed = { viewModel.close() })
                          }

                      Box(modifier = Modifier.height(Dimens.small))

                      HorizontalDivider()

                      // Content
                      Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(.85f)) {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            horizontalArrangement =
                                Arrangement.spacedBy(
                                    Dimens.small,
                                ),
                            verticalItemSpacing = Dimens.small,
                            contentPadding =
                                PaddingValues(
                                    vertical = Dimens.small,
                                )) {
                              items(items = whatsNew) { releaseNote ->
                                Column {
                                  Text(
                                      releaseNote.title,
                                      color = AppColor.White,
                                      textAlign = TextAlign.Start,
                                      fontWeight = FontWeight.SemiBold,
                                      style = MaterialTheme.typography.bodySmall,
                                      lineHeight = 1.em,
                                  )
                                  Box(modifier = Modifier.height(2.dp))
                                  Text(
                                      releaseNote.description,
                                      color = AppColor.White60,
                                      textAlign = TextAlign.Start,
                                      style = MaterialTheme.typography.bodySmall,
                                      fontSize = 8.sp,
                                      lineHeight = 1.em,
                                  )
                                }
                              }
                            }
                      }

                      HorizontalDivider()

                      Box(modifier = Modifier.fillMaxWidth().weight(1f))

                      Row(
                          modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                          horizontalArrangement = Arrangement.End,
                          verticalAlignment = Alignment.CenterVertically,
                      ) {


                        RoundedButton(
                            onClick = { viewModel.close() },
                            title = stringResource(R.string.continue_button),
                        )
                      }
                    }
              }
            }
      }
    }
  }
}