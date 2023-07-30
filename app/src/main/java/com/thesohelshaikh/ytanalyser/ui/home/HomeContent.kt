package com.thesohelshaikh.ytanalyser.ui.home

import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.ui.details.DurationsManger

@Composable
fun HomeContent(
    receivedUrl: String?,
    onClickAnalyse: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = stringResource(
                id = R.string.cd_app_logo
            ),
            modifier = Modifier
                .padding(16.dp)
                .size(200.dp)
        )

        var urlInput by remember { mutableStateOf("") }
        val context = LocalContext.current

        if (!receivedUrl.isNullOrEmpty()) urlInput = receivedUrl

        LifecycleEventListener {
            if (urlInput.isNotEmpty()) return@LifecycleEventListener
            urlInput = getStringFromClipboard(context)
        }

        OutlinedTextField(
            value = urlInput,
            onValueChange = { urlInput = it },
            label = { Text(stringResource(id = R.string.hint_playlist_id_video_id_url)) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            trailingIcon = {
                if (urlInput.isEmpty()) return@OutlinedTextField

                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = stringResource(R.string.cd_clear_text),
                    modifier = Modifier.clickable { urlInput = "" }
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(
                onGo = {
                    val videoId = validateUrl(urlInput) ?: return@KeyboardActions
                    onClickAnalyse(videoId)
                }
            ),
        )

        Button(
            onClick = {
                val videoId = validateUrl(urlInput) ?: return@Button
                onClickAnalyse(videoId)
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.5f)
        ) {
            Text(text = stringResource(id = R.string.button_analyse))
        }
    }
}

@Composable
private fun LifecycleEventListener(onResume: () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnResume by rememberUpdatedState(onResume)

    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentOnResume()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

private fun validateUrl(urlInput: String): String? {
    if (urlInput.isBlank() || urlInput.isEmpty()) return null
    return DurationsManger.getIDfromURL(urlInput)
}

private fun getStringFromClipboard(context: Context): String {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipText = clipboardManager.primaryClip?.getItemAt(0)?.text ?: ""
    Log.d("TAG", "getStringFromClipboard: $clipText, ${clipboardManager.primaryClip}")
    if (clipText.startsWith("http")) return clipText.toString()
    return ""
}