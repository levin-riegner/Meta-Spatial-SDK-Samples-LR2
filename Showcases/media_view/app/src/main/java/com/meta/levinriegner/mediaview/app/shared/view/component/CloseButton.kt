// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.shared.view.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.spatial.uiset.button.BorderedIconButton

@Composable
fun CloseButton(
    onPressed: () -> Unit,
) {
    BorderedIconButton(
        modifier = Modifier.size(Dimens.large),
        icon = {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Close",
                tint = AppColor.White,
            )
        },
        onClick = onPressed,
        borderColor = AppColor.White30,
    )
}
