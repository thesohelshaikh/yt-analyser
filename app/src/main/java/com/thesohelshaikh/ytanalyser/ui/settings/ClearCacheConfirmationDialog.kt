package com.thesohelshaikh.ytanalyser.ui.settings

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
fun ClearCacheConfirmationDialog(
    showDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        confirmButton = {
            TextButton(
                modifier = Modifier,
                onClick = {
                    showDialog.value = false
                    onConfirm()
                }
            ) {
                Text("Clear")
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier,
                onClick = { showDialog.value = false }
            ) {
                Text("Dismiss")
            }
        },
        title = {
            Text(text = "Clear local app cache?")
        },
        text = {
            Text(
                "This will clear entire local history, including videos and playlists."
            )
        }
    )
}