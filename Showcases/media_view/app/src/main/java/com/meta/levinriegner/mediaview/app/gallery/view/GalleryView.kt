// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.gallery.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import com.meta.spatial.uiset.control.SpatialSwitch
import com.meta.spatial.uiset.control.SwitchDefaults
import com.meta.spatial.uiset.dropdown.SpatialDropdown
import com.meta.spatial.uiset.dropdown.foundation.SpatialDropdownItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.meta.levinriegner.mediaview.R
import com.meta.levinriegner.mediaview.app.gallery.filter.titleResId
import com.meta.levinriegner.mediaview.app.onboarding.view.OnboardingButton
import com.meta.levinriegner.mediaview.app.shared.model.UiState
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.levinriegner.mediaview.app.shared.theme.MediaViewTheme
import com.meta.levinriegner.mediaview.app.shared.view.ErrorView
import com.meta.levinriegner.mediaview.app.shared.view.LoadingView
import com.meta.levinriegner.mediaview.data.gallery.model.MediaFilter
import com.meta.levinriegner.mediaview.data.gallery.model.MediaModel
import com.meta.levinriegner.mediaview.data.gallery.model.MediaSortBy

@Composable
fun GalleryView(
    uiState: UiState<List<MediaModel>>,
    filter: MediaFilter,
    sortBy: MediaSortBy,
    showMetadata: Boolean,
    onRefresh: () -> Unit,
    onMediaSelected: (MediaModel) -> Unit,
    onSortBy: (MediaSortBy) -> Unit,
    onToggleMetadata: (Boolean) -> Unit,
    onOnboardingButtonPressed: () -> Unit,
) {
  MediaViewTheme {
    Scaffold(
        modifier =
            Modifier.fillMaxSize()
                .border(
                    width = 1.dp,
                    color = AppColor.MetaBlu,
                    shape = RoundedCornerShape(Dimens.radiusMedium),
                )
                .clip(RoundedCornerShape(Dimens.radiusMedium))
    ) { innerPadding ->
      when (uiState) {
        UiState.Idle -> Box(Modifier)
        UiState.Loading -> LoadingView(modifier = Modifier.fillMaxSize())
        is UiState.Success ->
            Column(
                modifier = Modifier.fillMaxSize().background(AppColor.BackgroundSweep),
            ) {
              Header(
                  filter = filter,
                  sortBy = sortBy,
                  onSortBy = onSortBy,
                  fileCount = uiState.data.size,
                  showMetadata = showMetadata,
                  onToggleMetadata = onToggleMetadata,
                  onOnboardingButtonPressed = onOnboardingButtonPressed,
              )
              MediaGrid(
                  media = uiState.data,
                  showMetadata = showMetadata,
                  modifier = Modifier.padding(innerPadding),
                  onItemClicked = onMediaSelected,
              )
            }

        is UiState.Error ->
            ErrorView(
                modifier = Modifier.fillMaxSize(),
                description = uiState.message,
                onActionButtonPressed = onRefresh,
            )
      }
    }
  }
}

@Composable
private fun Header(
    filter: MediaFilter,
    sortBy: MediaSortBy,
    onSortBy: (MediaSortBy) -> Unit,
    fileCount: Int,
    showMetadata: Boolean,
    onToggleMetadata: (Boolean) -> Unit,
    onOnboardingButtonPressed: () -> Unit,
) {
  Column {
    Box(modifier = Modifier.padding(Dimens.medium)) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Column {
          Text(
              text = stringResource(filter.titleResId()),
              style = MaterialTheme.typography.titleMedium.copy(color = AppColor.White),
          )
          Spacer(Modifier.size(Dimens.xSmall))
          val resources = LocalContext.current.resources
          Text(
              text =
                  if (fileCount == 0) {
                    stringResource(R.string.no_files)
                  } else {
                    resources.getQuantityString(R.plurals.viewing_n_files, fileCount, fileCount)
                  },
              style = MaterialTheme.typography.bodySmall.copy(color = AppColor.White60),
          )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
          OnboardingButton(onPressed = onOnboardingButtonPressed)
          Box(modifier = Modifier.size(Dimens.medium))

          val sortByItems = remember {
            MediaSortBy.entries.map { option ->
              SpatialDropdownItem(
                title = when (option) {
                  MediaSortBy.DateDesc -> "Date Added: Earliest"
                  MediaSortBy.DateAsc -> "Date Added: Oldest"
                  MediaSortBy.SizeAsc -> "File Size: Smallest"
                  MediaSortBy.SizeDesc -> "File Size: Largest"
                  MediaSortBy.NameAsc -> "Name: A to Z"
                  MediaSortBy.NameDesc -> "Name: Z to A"
                }
              )
            }
          }

          SpatialDropdown(
              modifier = Modifier.height(40.dp).width(120.dp).border(1.dp, AppColor.White15, RoundedCornerShape(Dimens.radiusXLarge)),
              filled = false,
              leading = {
                Icon(
                    painter = painterResource(id = R.drawable.icon_sortby),
                    contentDescription = "Sort by icon",
                    tint = AppColor.White
                )
              },
              title = stringResource(id = R.string.sort_by),
              items = sortByItems,
              selectedItem = null,
              showChevron = false,
              menuModifier = Modifier
                  .background(AppColor.BackgroundSweep)
                  .border(1.dp, AppColor.MetaBlu, RoundedCornerShape(12.dp)),
              showDividers = true,
              onItemSelected = { item ->
                val selectedSortBy = MediaSortBy.entries[sortByItems.indexOf(item)]
                onSortBy(selectedSortBy)
              }
          )

          Box(modifier = Modifier.size(Dimens.medium))
          SpatialSwitch(
              thumbContent = {
                Icon(
                    Icons.Sharp.Info,
                    "Toggle media info",
                )
              },
              checked = showMetadata,
              onCheckedChange = { onToggleMetadata(it) },
              colors = SwitchDefaults.colors().copy(
                  uncheckedThumbColor = AppColor.White60,
                  uncheckedBorderColor = Color.Transparent,
                  uncheckedTrackColor = AppColor.White15,
                  checkedThumbColor = Color.White,
                  checkedBorderColor = Color.Transparent,
                  checkedTrackColor = AppColor.White15,
                  checkedIconColor = AppColor.MetaBlu,
              ),
          )
        }
      }
    }
    HorizontalDivider(color = AppColor.White15, thickness = 1.dp)
  }
}

@Composable
private fun MediaGrid(
    media: List<MediaModel>,
    modifier: Modifier = Modifier,
    showMetadata: Boolean,
    onItemClicked: (MediaModel) -> Unit,
) {
  LazyVerticalGrid(
      modifier = modifier,
      contentPadding = PaddingValues(Dimens.large),
      verticalArrangement = Arrangement.spacedBy(Dimens.small),
      horizontalArrangement = Arrangement.spacedBy(Dimens.small),
      columns = GridCells.Adaptive(Dimens.galleryItemSize),
  ) {
    items(media.size) { index ->
      MediaItemView(
          media[index],
          showMetadata,
          onItemClicked = onItemClicked,
      )
    }
  }
}
