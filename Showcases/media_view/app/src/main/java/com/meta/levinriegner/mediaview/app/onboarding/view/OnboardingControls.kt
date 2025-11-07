// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.onboarding.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.spatial.uiset.button.BorderedIconButton

@Composable
fun OnboardingControls(
    onPreviousButtonPressed: () -> Unit,
    onNextButtonPressed: () -> Unit,
    onFinishButtonPressed: (() -> Unit)?,
    currentStep: Int,
    totalSteps: Int,
) {
  Row(
      modifier = Modifier.fillMaxSize(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically) {
        BorderedIconButton(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go to previous step",
                    tint = AppColor.White,
                )
            },
            modifier = Modifier.size(Dimens.large),
            borderColor = AppColor.White30,
            onClick = onPreviousButtonPressed
        )
        Text(
            "$currentStep of $totalSteps",
            style = MaterialTheme.typography.bodySmall,
            color = AppColor.White30,
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
        )

        if (onFinishButtonPressed != null) {
            BorderedIconButton(
                icon = {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "Finish",
                        tint = AppColor.MetaBlu,
                    )
                },
                modifier = Modifier.size(Dimens.large),
                borderColor = AppColor.MetaBlu,
                onClick = onFinishButtonPressed
            )
        } else {
            BorderedIconButton(
                icon = {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Go to next step",
                        tint = AppColor.White,
                    )
                },
                modifier = Modifier.size(Dimens.large),
                borderColor = AppColor.MetaBlu,
                onClick = onNextButtonPressed
            )
        }
      }
}