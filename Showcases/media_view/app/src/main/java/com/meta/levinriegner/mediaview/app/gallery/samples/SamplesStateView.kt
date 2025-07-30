// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.gallery.samples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.meta.levinriegner.mediaview.R
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.levinriegner.mediaview.data.samples.model.SamplesList
import com.meta.levinriegner.mediaview.app.shared.view.component.BannerView
import com.meta.spatial.uiset.button.BorderedButton
import com.meta.spatial.uiset.button.BorderlessButton

@Composable
fun SamplesStateView(
    modifier: Modifier,
    state: UiSamplesState,
    onDownload: (SamplesList) -> Unit,
    onRefresh: () -> Unit,
    onDismiss: () -> Unit,
) {
  when (state) {
    is UiSamplesState.Idle -> {
      Box(Modifier)
    }
    is UiSamplesState.NoInternet -> {
      BannerView(
        modifier = modifier,
        label = stringResource(R.string.sample_media_no_internet),
        buttons = {
          BorderedButton(
            label = stringResource(R.string.retry),
            onClick = onRefresh,
            borderColor = AppColor.White,
          )
          Spacer(modifier = Modifier.size(Dimens.medium))
          BorderlessButton(
            label = stringResource(R.string.dismiss),
            onClick = { onDismiss() },
          )
        }
      )
    }
    is UiSamplesState.Loading -> {
      BannerView(
        modifier = modifier,
        label = stringResource(R.string.sample_media_loading),
        buttons = {
          CircularProgressIndicator(
            color = AppColor.White,
          )
        }
      )
    }
    is UiSamplesState.NewSamplesAvailable -> {
      BannerView(
        modifier = modifier,
        label = stringResource(R.string.sample_media_available),
        buttons = {
          BorderedButton(
            label = stringResource(R.string.download),
            onClick = { onDownload(state.samples) },
            borderColor = AppColor.White,
          )
          Spacer(modifier = Modifier.size(Dimens.medium))
          BorderlessButton(
            label = stringResource(R.string.dismiss),
            onClick = { onDismiss() },
          )
        }
      )
    }
    is UiSamplesState.DownloadingSamples -> {
      BannerView(
        modifier = modifier,
        label = stringResource(R.string.sample_media_downloading, state.current, state.total),
        buttons = {
          CircularProgressIndicator(
            color = AppColor.White,
          )
        }
      )
    }
    is UiSamplesState.DownloadError -> {
      BannerView(
        modifier = modifier,
        label = stringResource(R.string.sample_media_error, state.message),
        buttons = {
          BorderedButton(
            label = stringResource(R.string.retry),
            onClick = onRefresh,
            borderColor = AppColor.White,
          )
          Spacer(modifier = Modifier.size(Dimens.medium))
          BorderlessButton(
            label = stringResource(R.string.dismiss),
            onClick = { onDismiss() },
          )
        }
      )
    }
    is UiSamplesState.DownloadSuccess -> {
      BannerView(
        modifier = modifier,
        label = stringResource(R.string.sample_media_success),
        buttons = {
          BorderlessButton(
            label = stringResource(R.string.dismiss),
            onClick = { onDismiss() },
          )
        }
      )
    }
  }
}
