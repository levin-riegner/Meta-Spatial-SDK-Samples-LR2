// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.gallery.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.meta.levinriegner.mediaview.R
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.levinriegner.mediaview.app.shared.view.component.BannerView

@Composable
fun GalleryMenuView(
    openCount: Int,
    canOpenMore: Boolean,
    onCloseAll: () -> Unit,
) {
  if (openCount == 0) return Box(Modifier)
  BannerView(
    label = if (canOpenMore)
      pluralStringResource(
        id = R.plurals.n_files_open,
        openCount,
        openCount,
      )
    else stringResource(id = R.string.max_files_open, openCount),
    buttons = {
      OutlinedButton(
        colors = ButtonDefaults.buttonColors(
          contentColor = AppColor.White,
          containerColor = Color.Transparent,
        ),
        onClick = { onCloseAll() }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            painter = painterResource(id = R.drawable.icon_close),
            contentDescription = "Close")
          Spacer(modifier = Modifier.size(Dimens.xSmall))
          Text(stringResource(id = R.string.files_close_all_button))
        }
      }
    }
  )
}
