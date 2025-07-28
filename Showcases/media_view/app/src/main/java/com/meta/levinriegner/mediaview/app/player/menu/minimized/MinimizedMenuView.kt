// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.player.menu.minimized

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoDelete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.meta.levinriegner.mediaview.R
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens
import com.meta.spatial.uiset.dropdown.SpatialIconDropdown
import com.meta.spatial.uiset.dropdown.foundation.SpatialDropdownItem
import timber.log.Timber

@Composable
fun MinimizedMenuView(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit,
) {
  val confirmingDelete = remember { mutableStateOf(false) }

  val menuItems =
      remember(confirmingDelete.value) {
        listOf(
            SpatialDropdownItem(
                leading = {
                  Icon(
                      painter = painterResource(id = R.drawable.icon_immersive_view),
                      contentDescription = null,
                      modifier = Modifier.size(30.dp),
                  )
                },
                title = "Immersive View",
            ),
            SpatialDropdownItem(
                leading = {
                  Icon(
                      imageVector =
                          if (!confirmingDelete.value) Icons.Outlined.Delete
                          else Icons.Outlined.AutoDelete,
                      contentDescription = null,
                      modifier = Modifier.size(30.dp),
                  )
                },
                title = if (!confirmingDelete.value) "Delete" else "Confirm Delete",
            ),
            SpatialDropdownItem(
                leading = {
                  Icon(
                      painter = painterResource(id = R.drawable.icon_close),
                      contentDescription = null,
                      modifier = Modifier.size(30.dp),
                  )
                },
                title = "Close",
            ),
        )
      }

  SpatialIconDropdown(
      modifier = modifier,
      filled = false,
      icon = {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier.size(Dimens.playerMenuButtonSize.dp)
                    .aspectRatio(1f)
                    .background(
                        Brush.verticalGradient(listOf(AppColor.GradientStart, AppColor.GradientEnd)),
                        shape = CircleShape,
                    )
                    .border(
                        width = 1.dp,
                        color = AppColor.MetaBlu,
                        shape = RoundedCornerShape((Dimens.playerMenuButtonSize / 2).dp),
                    ),
        ) {
          Icon(
              painter = painterResource(id = R.drawable.icon_menu_dots_horizontal),
              contentDescription = "Menu",
              tint = AppColor.White,
          )
        }
      },
      items = menuItems,
      onItemSelected = {
        when (menuItems.indexOf(it)) {
          0 -> onMaximize()
          1 -> {
            if (confirmingDelete.value) {
              onDelete()
            } else {
              Timber.i("Confirming delete")
            }
            confirmingDelete.value = !confirmingDelete.value
          }
          2 -> onClose()
        }
      },
      showChevron = false,
      menuModifier =
          Modifier.shadow(2.dp)
              .border(1.dp, AppColor.MetaBlu, RoundedCornerShape(4.dp))
              .fillMaxWidth()
              .background(
                  Brush.verticalGradient(listOf(AppColor.GradientStart, AppColor.GradientEnd)),
              ),
      showDividers = true,
  )
}
