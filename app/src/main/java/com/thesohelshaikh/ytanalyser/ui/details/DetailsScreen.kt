package com.thesohelshaikh.ytanalyser.ui.details

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thesohelshaikh.ytanalyser.R
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date


@Composable
fun DetailsScreen(
    videoId: String,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit, block = {
        if (videoId.startsWith("PL")) {
            detailsViewModel.getPlaylistVideoIds(videoId)
        } else {
            detailsViewModel.getVideoDetails(videoId)
        }
    })

    val state by detailsViewModel.detailsScreenState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    when (state) {
        is DetailsViewModel.DetailsScreenState.ErrorState -> {

        }

        DetailsViewModel.DetailsScreenState.LoadingState -> {
            Timber.d("DetailsScreen: $state")
            LoadingState()
        }

        is DetailsViewModel.DetailsScreenState.SuccessState -> {
            val successState = state as DetailsViewModel.DetailsScreenState.SuccessState
            Timber.d("DetailsScreen: $state")
            DurationsList(
                successState.id,
                successState.thumbnailUrl,
                successState.title,
                successState.channelTitle,
                successState.duration,
                onShareClick = {
                    detailsViewModel.shareDetailsScreenshot(context, it)
                }
            )
        }

        DetailsViewModel.DetailsScreenState.InitialState -> {
            /* no-op */
        }
    }

}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(16.dp)
        )
        Text(text = "Loading...")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun DurationsList(
    id: String,
    thumbnailUrl: String?,
    title: String?,
    channelTitle: String?,
    duration: Long,
    onShareClick: (Bitmap) -> Unit = {}
) {
    val alternateDurations = DurationsManger.calculateAlternateDurations(Date(duration))
    val playbacks = mutableListOf<String>()
    playbacks.add("1x")
    playbacks.add("1.25x")
    playbacks.add("1.5x")
    playbacks.add("1.75x")
    playbacks.add("2x")

    val context = LocalContext.current
    val captureController = rememberCaptureController()
    LazyColumn(
        modifier = Modifier
            .capturable(captureController)
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .fillMaxSize()

    ) {
        val horizontalMargin = 16.dp
        item {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(thumbnailUrl)
                        .crossfade(true)
                        .build(), contentDescription = stringResource(R.string.cd_thumbnail),
                    modifier = Modifier
                        .aspectRatio(16f / 9f)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(horizontalMargin)),
                    contentScale = ContentScale.Crop
                )

                Icon(imageVector = Icons.Filled.PlayCircle,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(64.dp)
                        .clickable { openYoutube(context, id) }
                )
            }

            Text(
                text = title ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = horizontalMargin, end = horizontalMargin)
                    .fillMaxWidth()
            )
            Row {
                Column(
                    Modifier.weight(1f)
                ) {
                    Text(
                        text = channelTitle ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = horizontalMargin),
                    )
                    Text(
                        text = DurationsManger.getPrettyDuration(duration),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = horizontalMargin)
                    )
                }
                ShareScreenshotButton(captureController, onShareClick)

            }

            Spacer(modifier = Modifier.height(32.dp))
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalMargin),
            ) {
                Text(
                    text = stringResource(R.string.label_playback_speed),
                    modifier = Modifier.fillParentMaxWidth(0.3f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,

                    )
                Text(
                    text = stringResource(id = R.string.label_to_complete),
                    modifier = Modifier.fillParentMaxWidth(0.3f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(id = R.string.label_complete_by),
                    modifier = Modifier.fillParentMaxWidth(0.4f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        itemsIndexed(alternateDurations) { index, alternateDuration ->
            DurationRow(alternateDuration, playbacks[index])
            if (index != alternateDurations.lastIndex) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeApi::class)
private fun ShareScreenshotButton(
    captureController: CaptureController,
    onShareClick: (Bitmap) -> Unit
) {
    val scope = rememberCoroutineScope()

    IconButton(
        modifier = Modifier.padding(end = 8.dp),
        onClick = {
            scope.launch {
                val bitmapAsync = captureController.captureAsync()
                try {
                    val bitmap = bitmapAsync.await()
                    onShareClick(bitmap.asAndroidBitmap())
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }) {
        Icon(imageVector = Icons.Outlined.Share, contentDescription = null)
    }
}

@Composable
private fun DurationRow(alternateDuration: Long, playbackSpeed: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = playbackSpeed, modifier = Modifier.weight(0.3f), textAlign = TextAlign.Center
        )
        Text(
            text = DurationsManger.getPrettyDuration(alternateDuration),
            modifier = Modifier.weight(0.3f),
            textAlign = TextAlign.Center

        )
        Text(
            text = DurationsManger.getDateAfter(alternateDuration),
            modifier = Modifier.weight(0.4f),
            textAlign = TextAlign.Center

        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

private fun openYoutube(context: Context, id: String) {
    val webIntent = Intent(
        Intent.ACTION_VIEW, Uri.parse(DurationsManger.getUrlFromId(id))
    )
    try {
        context.startActivity(webIntent)
    } catch (e: ActivityNotFoundException) {
        Timber.e(e)
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    Column {
        DurationsList(
            "",
            "",
            "Video title",
            "Channel Name",
            123123L
        )
    }
}