package com.thesohelshaikh.ytanalyser.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.data.model.DarkThemeConfig
import com.thesohelshaikh.ytanalyser.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseThemeDialog(
    showDialog: MutableState<Boolean>,
    theme: DarkThemeConfig,
    onThemeUpdate: (theme: DarkThemeConfig) -> Unit,
) {
    var selectedTheme by remember { mutableStateOf(theme) }

    BasicAlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        properties = DialogProperties(),
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {

                Column {
                    Text(
                        text = stringResource(id = R.string.dialog_title_choose_theme),
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    HorizontalDivider()
                    DarkThemeConfig.values().forEachIndexed { index, theme ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = selectedTheme == theme,
                                    role = Role.RadioButton,
                                    onClick = {
                                        selectedTheme = theme
                                        onThemeUpdate(theme)
                                    },
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = selectedTheme == theme,
                                onClick = null,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(id = theme.displayValue))
                        }
                    }
                    HorizontalDivider()
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = { showDialog.value = false }
                        ) {
                            Text(stringResource(R.string.button_ok))
                        }
                    }
                }
            }
        })
}

@Preview
@Composable
fun ChooseThemeDialogPreview() {
    AppTheme {
        ChooseThemeDialog(
            showDialog = remember { mutableStateOf(true) },
            DarkThemeConfig.FOLLOWS_SYSTEM
        ) {
            /* no-op */
        }
    }
}