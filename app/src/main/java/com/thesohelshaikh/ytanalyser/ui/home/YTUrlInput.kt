package com.thesohelshaikh.ytanalyser.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.ui.details.DurationsManger

@Composable
fun YTUrlInput(
    defaultVal: String,
    onUrlChange: (String) -> Unit,
    onClickAnalyse: () -> Unit,
) {
    OutlinedTextField(
        value = defaultVal,
        onValueChange = { onUrlChange(it) },
        label = { Text(stringResource(id = R.string.hint_playlist_id_video_id_url)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        trailingIcon = {
            if (defaultVal.isEmpty()) return@OutlinedTextField

            Icon(
                imageVector = Icons.Outlined.Cancel,
                contentDescription = stringResource(R.string.cd_clear_text),
                modifier = Modifier.clickable { onUrlChange("") }
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
        keyboardActions = KeyboardActions(
            onGo = {
                onClickAnalyse()
            }
        ),
        singleLine = true,
        leadingIcon = {
            Icon(imageVector = Icons.Outlined.Link, contentDescription = null)
        }
    )
}

fun validateUrl(urlInput: String): String? {
    if (urlInput.isBlank() || urlInput.isEmpty()) return null
    val idFromURL = DurationsManger.getIDfromURL(urlInput)
    if (idFromURL.isNullOrBlank() || idFromURL.isEmpty()) return null
    return idFromURL
}

@Preview(showBackground = true)
@Composable
private fun PreviewInput() {
    MaterialTheme {
        YTUrlInput(defaultVal = "", onUrlChange = {}, onClickAnalyse = {})
    }
}