// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.shared.view.component

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.MediaViewTheme
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import com.meta.spatial.uiset.button.BorderlessCircleButton
import com.meta.spatial.uiset.button.PrimaryCircleButton
import com.meta.spatial.uiset.button.SecondaryCircleButton

@Composable
fun MVCircleButton(
  modifier: Modifier = Modifier,
  fillColor: Color,
  onClick: () -> Unit,
  icon: @Composable (() -> Unit),
) {
  Box(
      modifier
          .background(color = fillColor, shape = RoundedCornerShape(50))
  ) {
    SecondaryCircleButton(
        icon = icon,
        onClick = onClick,
        isEnabled = true,
    )
  }
}

@Preview
@Composable
fun MVCircleButtonPreview() {
  MediaViewTheme {
    MVCircleButton(
        onClick = {},
        fillColor = AppColor.MetaBlu,
        icon = {
          Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "Check",
            tint = AppColor.White
          )
        },
    )
  }
}
