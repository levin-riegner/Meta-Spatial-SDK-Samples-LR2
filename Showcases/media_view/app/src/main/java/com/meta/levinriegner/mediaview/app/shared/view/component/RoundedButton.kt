// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.shared.view.component

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.spatial.uiset.button.BorderedButton

@Composable
fun RoundedButton(title: String, onClick: () -> Unit) {
  BorderedButton(
      label = title,
      onClick = onClick,
      modifier = Modifier.height(28.dp),
      borderColor = AppColor.MetaBlu,
      labelTextStyle = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold),
  )
}
