package com.home.artz.view.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.home.artz.R
import com.home.artz.model.datamodel.Artwork

@Composable
fun FavoriteArtworkRemoveDialog(
    shouldShowDialog: MutableState<Artwork?>,
    onConfirmClicked: (Artwork) -> Unit
) {
    if (shouldShowDialog.value != null) {
        AlertDialog(
            title = {
                Text(text = stringResource(R.string.favorite_dialog_remove_title))
            },
            onDismissRequest = { shouldShowDialog.value = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        shouldShowDialog.value?.let { onConfirmClicked.invoke(it) }
                        shouldShowDialog.value = null
                    }
                ) {
                    Text(stringResource(id = R.string.favorite_dialog_confirm_button_title))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        shouldShowDialog.value = null
                    }
                ) {
                    Text(stringResource(id = R.string.favorite_dialog_dismiss_button_title))
                }
            }
        )
    }
}