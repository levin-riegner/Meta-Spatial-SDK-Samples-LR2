// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.onboarding.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meta.levinriegner.mediaview.app.shared.Constants
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.spatial.uiset.button.PrimaryButton
import com.meta.spatial.uiset.button.foundation.PrimaryButtonDefaults
import androidx.compose.ui.text.font.FontWeight

@Composable
fun OnboardingTermsOfService(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit = {},
) {
  val uriHandler = LocalUriHandler.current

  Column(
      modifier = modifier
          .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    PrimaryButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        onClick = onContinue,
        label = "Continue",
        colors = PrimaryButtonDefaults.colors(
            backgroundColor = AppColor.MetaBlu,
            foregroundColor = AppColor.White
        ),
        expanded = true,
        labelTextStyle = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold)
    )
     Spacer(modifier = Modifier.height(Dimens.xxSmall))
    Row(
        modifier = Modifier.padding(horizontal = Dimens.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "By clicking Continue you agree to the ",
            style = MaterialTheme.typography.bodySmall.copy(
                color = AppColor.White60, 
                textAlign = TextAlign.Center,
                fontSize = 6.sp
            ),
        )
        Box(
            modifier = Modifier
                .clickable {
                    uriHandler.openUri(Constants.TERMS_AND_CONDITIONS_URL)
                }
        ) {
          Text(
              text = "Terms of Service",
              style = MaterialTheme.typography.bodySmall.copy(
                  color = AppColor.White60,
                  textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                  fontSize = 6.sp
              ),
          )
        }
    }
  }
}
