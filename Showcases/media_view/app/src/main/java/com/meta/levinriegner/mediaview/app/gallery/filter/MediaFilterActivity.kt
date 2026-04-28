// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.gallery.filter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.meta.levinriegner.mediaview.R
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.levinriegner.mediaview.app.shared.theme.MediaViewTheme
import dagger.hilt.android.AndroidEntryPoint
import com.meta.spatial.uiset.navigation.SpatialSideNavItem
import com.meta.spatial.uiset.navigation.foundation.SideNavItemDefaults
import com.meta.spatial.uiset.button.BorderedButton
import com.meta.spatial.uiset.button.foundation.BorderedButtonDefaults

@AndroidEntryPoint
class MediaFilterActivity : ComponentActivity() {

  private val viewModel: MediaFilterViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    buildUi()
  }

  private fun buildUi() {
    setContent {
      // Observables
      val filters = viewModel.filters.collectAsState()
      val isSelectionMode = viewModel.isSelectionMode.collectAsState()
      // UI
      MediaViewTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 4.dp, color = AppColor.MetaBlu, shape = RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(AppColor.BackgroundSweep)
        ) {
            FilterList(
                pickerFilter = filters.value,
                isSelectionMode = isSelectionMode.value,
                onFilterSelected = { viewModel.onFilterSelected(it) },
                onUpload = { viewModel.onUpload() }
            )
        }
      }
    }
  }
}

@Composable
private fun FilterList(
    pickerFilter: List<UiMediaFilter>,
    isSelectionMode: Boolean,
    onFilterSelected: (UiMediaFilter) -> Unit,
    onUpload: () -> Unit,
) {
  Column(modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 20.dp, vertical = 40.dp)) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(pickerFilter) { filter ->
            val isDisabled = isSelectionMode && !filter.isSelected
            SpatialSideNavItem(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 20.dp)
                    .fillMaxWidth()
                    .height(96.dp)
                    .clip(RoundedCornerShape(Dimens.xLarge)),
                icon = {
                    Icon(
                        painter = painterResource(id = filter.type.iconResId()),
                        contentDescription = "Button Icon",
                        tint = if (isDisabled) AppColor.White60 else AppColor.White,
                        modifier = Modifier
                            .size(128.dp)
                            .padding(end = Dimens.xLarge)
                    )
                },
                onClick = { if (!isDisabled) onFilterSelected(filter) },
                primaryLabel = stringResource(filter.type.titleResId()),
                selected = filter.isSelected,
                showExpandedIcon = true,
                dense = false,
                primaryTextStyle = TextStyle(
                    fontSize = 35.sp, 
                    fontWeight = FontWeight.Bold
                ),
                selectedBackgroundColor = AppColor.ButtonSelect,
                colors = if (isDisabled) {
                    SideNavItemDefaults.colors(
                        unselectedPrimaryColor = AppColor.White60
                    )
                } else {
                    SideNavItemDefaults.colors()
                }
            )
        }
    }

     Row(
         modifier = Modifier
             .fillMaxWidth()
             .weight(0.5F)
             .background(Color.Transparent),
         horizontalArrangement = Arrangement.Center,
         verticalAlignment = Alignment.CenterVertically) {}


    //   BorderedButton(
    //       modifier =
    //           Modifier
    //               .padding(horizontal = 10.dp, vertical = 20.dp)
    //               .height(96.dp)
    //               .fillMaxWidth(),
    //       label = stringResource(R.string.add_media),
    //       onClick = { if (!isSelectionMode) onUpload() },
    //       borderColor = if (isSelectionMode) AppColor.White30 else AppColor.White60,
    //       labelTextStyle = TextStyle(
    //           fontSize = 35.sp, 
    //             fontWeight = FontWeight.Bold
    //       ),
    //       expanded = true,
    //       leading = {
    //         Row {
    //           Icon(
    //               painter = painterResource(id = R.drawable.icon_upload),
    //               contentDescription = stringResource(R.string.add_media),
    //               tint = if (isSelectionMode) AppColor.White60 else AppColor.White,
    //               modifier = Modifier
    //                   .size(84.dp)
    //                   .padding(start = 0.dp, end = 0.dp)
    //           )
    //           Spacer(modifier = Modifier.width(40.dp))

    //         }
    //       },
    //       contentAlignment = Alignment.Start,
    //       colors = if (isSelectionMode) {
    //           // Use custom colors for disabled state - foregroundColor controls text color
    //           BorderedButtonDefaults.colors(
    //               foregroundColor = AppColor.White60
    //           )
    //       } else {
    //           // Use default colors for enabled state
    //           BorderedButtonDefaults.colors()
    //       }
    //   )
  }
}
