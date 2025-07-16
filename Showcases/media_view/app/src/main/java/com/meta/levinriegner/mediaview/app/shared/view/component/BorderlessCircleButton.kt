// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.shared.view.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.MediaViewTheme
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.spatial.uiset.button.BorderlessCircleButton

@Composable
fun MVBorderlessCircleButton(
  modifier: Modifier = Modifier,
  borderColor: Color = AppColor.White60,
  onClick: () -> Unit,
  icon: @Composable (() -> Unit),
) {
  Box(
      modifier
          .border(
              width = 0.1.dp,
              color = borderColor,
              shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
          ),
  ) {
    BorderlessCircleButton(
        icon = icon,
        onClick = onClick,
        isEnabled = true,
    )
  }
}

@Preview
@Composable
fun MVBorderlessCircleButtonPreview() {
  MediaViewTheme {
    MVBorderlessCircleButton(
        onClick = {},
        borderColor = AppColor.MetaBlu,
        icon = {
          Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Forward",
            modifier = Modifier.size(Dimens.large),
          )
        },
    )
  }
}
