// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.onboarding.view

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.spatial.uiset.button.BorderedIconButton

@Composable
fun OnboardingButton(
    onPressed: () -> Unit,
) {
  BorderedIconButton(
    modifier = Modifier.size(Dimens.xLarge),
    icon = {
      Icon(
        Icons.Filled.QuestionMark,
        contentDescription = "Open Onboarding",
        tint = AppColor.White,
      )
    },
    onClick = onPressed,
    isEnabled = true,
    borderColor = AppColor.White30
  )
}
