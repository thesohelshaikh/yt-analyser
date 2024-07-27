package com.thesohelshaikh.ytanalyser.ui.home

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.data.model.HistoryItem
import com.thesohelshaikh.ytanalyser.ui.theme.AppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun HomeContent(
    homeViewModel: HomeViewModel = hiltViewModel(),
    receivedUrl: String?,
    onClickAnalyse: (String) -> Unit,
    onViewHistory: () -> Unit,
) {
    var urlInput by remember { mutableStateOf("") }
    val context = LocalContext.current
    val historyItems = homeViewModel.historyItemsFlow.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = historyItems) {
        homeViewModel.getVideosAndPlaylists()
    }

    if (!receivedUrl.isNullOrEmpty()) urlInput = receivedUrl

    LifecycleEventListener {
        if (urlInput.isNotEmpty()) return@LifecycleEventListener

        val shouldUseClipboard = homeViewModel.useClipboard.first()
        Timber.i("Should use clipboard: $shouldUseClipboard")
        if (shouldUseClipboard) {
            urlInput = getStringFromClipboard(context)
        }
    }

    val invalidUrlMessage = stringResource(R.string.message_invalid_url)

    HomeContent(
        urlInput = urlInput,
        historyItems.value,
        onUrlChange = {
            urlInput = it
        },
        onClickAnalyse = {
            val videoId = validateUrl(urlInput)
            if (videoId.isNullOrEmpty()) {
                Toast.makeText(
                    context,
                    invalidUrlMessage,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                onClickAnalyse(videoId)
            }
        }, onHistoryItemClick = {
            onClickAnalyse(it)
        },
        onViewHistory = {
            onViewHistory()
        }
    )
}

@Composable
private fun HomeContent(
    urlInput: String,
    historyItems: List<HistoryItem>,
    onUrlChange: (String) -> Unit,
    onClickAnalyse: () -> Unit,
    onHistoryItemClick: (String) -> Unit,
    onViewHistory: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Card(
            modifier = Modifier.padding(16.dp)
        ) {
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

        YTUrlInput(
            modifier = Modifier.padding(horizontal = 16.dp),
            defaultVal = urlInput,
            onUrlChange = onUrlChange,
            onClickAnalyse = onClickAnalyse,
        )


        Button(
            onClick = {
                onClickAnalyse()
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
        Spacer(modifier = Modifier.height(16.dp))
        HistorySection(onViewHistory, historyItems, onHistoryItemClick)
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
        HomeContent(
            urlInput = "",
            emptyList(),
            onUrlChange = {},
            onClickAnalyse = {},
            onHistoryItemClick = {},
            onViewHistory = {},
        )
    }
}