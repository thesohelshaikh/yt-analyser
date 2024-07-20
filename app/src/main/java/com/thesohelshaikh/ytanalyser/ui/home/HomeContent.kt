package com.thesohelshaikh.ytanalyser.ui.home

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.ui.details.DurationsManger
import com.thesohelshaikh.ytanalyser.ui.theme.AppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun HomeContent(
    homeViewModel: HomeViewModel = hiltViewModel(),
    receivedUrl: String?,
    onClickAnalyse: (String) -> Unit
) {
    var urlInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (!receivedUrl.isNullOrEmpty()) urlInput = receivedUrl

    LifecycleEventListener {
        if (urlInput.isNotEmpty()) return@LifecycleEventListener

        val shouldUseClipboard = homeViewModel.useClipboard.first()
        Timber.i("Should use clipboard: $shouldUseClipboard")
        if (shouldUseClipboard) {
            urlInput = getStringFromClipboard(context)
        }
    }

    HomeContent(urlInput, context, onClickAnalyse)
}

@Composable
private fun HomeContent(
    urlInput: String,
    context: Context,
    onClickAnalyse: (String) -> Unit
) {
    var urlInput1 = urlInput
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Filled.Insights,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(50.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(R.string.label_home_tagline),
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = stringResource(R.string.label_home_tagline_desc),
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .padding(bottom = 8.dp, top = 8.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
        }

        val invalidUrlMessage = stringResource(R.string.message_invalid_url)
        OutlinedTextField(
            value = urlInput1,
            onValueChange = { urlInput1 = it },
            label = { Text(stringResource(id = R.string.hint_playlist_id_video_id_url)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            trailingIcon = {
                if (urlInput1.isEmpty()) return@OutlinedTextField

                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = stringResource(R.string.cd_clear_text),
                    modifier = Modifier.clickable { urlInput1 = "" }
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(
                onGo = {
                    val videoId = validateUrl(urlInput1)
                    if (videoId.isNullOrEmpty()) {
                        Toast.makeText(
                            context,
                            invalidUrlMessage,
                            Toast.LENGTH_LONG
                        ).show()
                        return@KeyboardActions
                    }
                    onClickAnalyse(videoId)
                }
            ),
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Outlined.Link, contentDescription = null)
            }
        )

        Button(
            onClick = {
                val videoId = validateUrl(urlInput1)
                if (videoId.isNullOrEmpty()) {
                    Toast.makeText(
                        context,
                        invalidUrlMessage,
                        Toast.LENGTH_LONG
                    ).show()
                    return@Button
                }
                onClickAnalyse(videoId)
            },
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(top = 8.dp),
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            Text(text = stringResource(id = R.string.button_analyse))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Icon(
                imageVector = Icons.Filled.AutoAwesome,
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun LifecycleEventListener(onResume: suspend () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnResume by rememberUpdatedState(onResume)

    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                lifecycleOwner.lifecycleScope.launch {
                    currentOnResume()
                }
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
    val idFromURL = DurationsManger.getIDfromURL(urlInput)
    if (idFromURL.isNullOrBlank() || idFromURL.isEmpty()) return null
    return idFromURL
}

private fun getStringFromClipboard(context: Context): String {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipText = clipboardManager.primaryClip?.getItemAt(0)?.text ?: ""
    Timber.d("getStringFromClipboard: " + clipText + ", " + clipboardManager.primaryClip)
    if (clipText.startsWith("http")) return clipText.toString()
    return ""
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeContent() {
    AppTheme {
        HomeContent(urlInput = "", context = LocalContext.current, onClickAnalyse = {})
    }
}