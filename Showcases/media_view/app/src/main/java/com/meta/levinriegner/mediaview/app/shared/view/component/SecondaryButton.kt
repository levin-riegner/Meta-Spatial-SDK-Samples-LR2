// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.shared.view.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.MediaViewTheme
import com.meta.spatial.uiset.button.BorderlessButton
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Icon
import com.meta.levinriegner.mediaview.R
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier

@Composable
fun MVSecondaryButton(
  modifier: Modifier = Modifier,
  title: String,
  onClick: () -> Unit,
  leading: @Composable (() -> Unit)? = null,
  expanded: Boolean = false,
) {
  Box(
    modifier
      .border(
        width = 0.1.dp,
        color = AppColor.White60,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
      ),
    contentAlignment = androidx.compose.ui.Alignment.Center
  ) {
    BorderlessButton(
      label = title,
      onClick = onClick,
      leading = leading,
      expanded = expanded,
    )
  }
}

@Preview
@Composable
fun MVSecondaryButtonPreview() {
  MediaViewTheme {
    MVSecondaryButton(
      title = "Download Media",
      onClick = {},
      leading = {
        Icon(
          painter = painterResource(id = R.drawable.icon_upload),
          contentDescription = "Close")
      }
    )
  }
}
