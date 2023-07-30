package com.thesohelshaikh.ytanalyser.ui.settings

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.ui.theme.AppTheme

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
                Text(stringResource(R.string.button_clear))
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier,
                onClick = { showDialog.value = false }
            ) {
                Text(stringResource(R.string.button_cancel))
            }
        },
        title = {
            Text(text = stringResource(R.string.dialog_title_clear_cache))
        },
        text = {
            Text(
                stringResource(R.string.dialog_message_clear_cache)
            )
        }
    )
}

@Preview
@Composable
fun ClearCacheConfirmationDialogPreview() {
    AppTheme {
        ClearCacheConfirmationDialog(
            showDialog = remember { mutableStateOf(true) }
        ) {
        }
    }
}