// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

package com.meta.levinriegner.mediaview.app.shared.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.meta.levinriegner.mediaview.R
import com.meta.levinriegner.mediaview.app.shared.theme.AppColor
import com.meta.levinriegner.mediaview.app.shared.theme.Dimens

/**
 * Data class representing a dialog button configuration
 */
data class DialogButton(
    val text: String,
    val onClick: () -> Unit,
    val isPrimary: Boolean = false,
    val isDestructive: Boolean = false
)

/**
 * Flexible dialog component that can be used for various purposes:
 * - Info dialogs (single button)
 * - Confirmation dialogs (two buttons)
 * - Delete dialogs (cancel + delete)
 * - Custom dialogs with custom button configurations
 */
@Composable
fun FlexibleDialog(
    title: String,
    description: String,
    icon: Int? = null,
    primaryButton: DialogButton,
    secondaryButton: DialogButton? = null,
    onDismiss: () -> Unit = {},
    dismissOnBackPress: Boolean = true,
    dismissOnClickOutside: Boolean = true
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, AppColor.MetaBlu, RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF2A2A2A),
                            Color(0xFF1A1A1A)
                        )
                    )
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Icon (optional)
                icon?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = AppColor.White
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.White,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColor.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Buttons
                if (secondaryButton != null) {
                    // Two buttons side by side
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Secondary button (left)
                        OutlinedButton(
                            onClick = secondaryButton.onClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (secondaryButton.isDestructive) Color.Red else AppColor.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (secondaryButton.isDestructive) Color.Red else AppColor.MetaBlu
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = secondaryButton.text,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }
                        
                        // Primary button (right)
                        Button(
                            onClick = primaryButton.onClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    primaryButton.isDestructive -> Color.Red
                                    primaryButton.isPrimary -> AppColor.MetaBlu
                                    else -> AppColor.MetaBlu
                                },
                                contentColor = AppColor.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = primaryButton.text,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    // Single button
                    Button(
                        onClick = primaryButton.onClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                primaryButton.isDestructive -> Color.Red
                                primaryButton.isPrimary -> AppColor.MetaBlu
                                else -> AppColor.MetaBlu
                            },
                            contentColor = AppColor.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = primaryButton.text,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Convenience function for a simple info dialog with one button
 */
@Composable
fun InfoDialog(
    title: String,
    description: String,
    buttonText: String = "OK",
    icon: Int? = null,
    onButtonClick: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    FlexibleDialog(
        title = title,
        description = description,
        icon = icon,
        primaryButton = DialogButton(
            text = buttonText,
            onClick = onButtonClick,
            isPrimary = true
        ),
        onDismiss = onDismiss
    )
}

/**
 * Convenience function for a confirmation dialog with two buttons
 */
@Composable
fun ConfirmationDialog(
    title: String,
    description: String,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    icon: Int? = null,
    onConfirm: () -> Unit,
    onCancel: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    FlexibleDialog(
        title = title,
        description = description,
        icon = icon,
        primaryButton = DialogButton(
            text = confirmText,
            onClick = onConfirm,
            isPrimary = true
        ),
        secondaryButton = DialogButton(
            text = cancelText,
            onClick = onCancel
        ),
        onDismiss = onDismiss
    )
}

/**
 * Convenience function for a delete confirmation dialog
 */
@Composable
fun DeleteConfirmationDialog(
    title: String = "Delete Confirmation",
    description: String,
    deleteText: String = "Delete",
    cancelText: String = "Cancel",
    icon: Int? = R.drawable.icon_delete,
    onDelete: () -> Unit,
    onCancel: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    FlexibleDialog(
        title = title,
        description = description,
        icon = icon,
        primaryButton = DialogButton(
            text = deleteText,
            onClick = onDelete,
            isPrimary = true,
            isDestructive = true
        ),
        secondaryButton = DialogButton(
            text = cancelText,
            onClick = onCancel
        ),
        onDismiss = onDismiss
    )
}
