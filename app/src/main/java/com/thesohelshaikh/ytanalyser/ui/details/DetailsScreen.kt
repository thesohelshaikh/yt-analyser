package com.thesohelshaikh.ytanalyser.ui.details

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thesohelshaikh.ytanalyser.R
import java.util.Date

@Composable
fun DetailsScreen(
    videoId: String,
    detailsViewModel: DetailsViewModel = viewModel(factory = DetailsViewModel.Factory)
) {
    LaunchedEffect(key1 = Unit, block = {
        if (videoId.startsWith("PL")) {
            detailsViewModel.getPlaylistVideoIds(videoId)
        } else {
            detailsViewModel.getVideoDetails(videoId)
        }
    })

    val state by detailsViewModel.detailsScreenState.observeAsState()

    when (state) {
        is DetailsViewModel.DetailsScreenState.ErrorState -> {

        }

        DetailsViewModel.DetailsScreenState.LoadingState -> {
            Log.d("TAG", "DetailsScreen: $state")
            LoadingState()
        }

        is DetailsViewModel.DetailsScreenState.SuccessState -> {
            val successState = state as DetailsViewModel.DetailsScreenState.SuccessState
            Log.d("TAG", "DetailsScreen: $state")
            DurationsList(
                successState.thumbnailUrl,
                successState.title,
                successState.channelTitle,
                successState.duration
            )
        }

        null -> {

        }
    }

}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.padding(16.dp)
        )
        Text(text = "Loading...")
    }
}

@Composable
private fun DurationsList(
    thumbnailUrl: String?,
    title: String?,
    channelTitle: String?,
    duration: Long
) {
    val alternateDurations = DurationsManger.calculateAlternateDurations(Date(duration))
    val playbacks = mutableListOf<String>()
    playbacks.add("1x")
    playbacks.add("1.25x")
    playbacks.add("1.5x")
    playbacks.add("1.75x")
    playbacks.add("2x")

    LazyColumn(
        modifier = Modifier
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
            Text(
                text = title ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = horizontalMargin, end = horizontalMargin)
            )
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
                    modifier = Modifier
                        .fillParentMaxWidth(0.3f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,

                    )
                Text(
                    text = stringResource(id = R.string.label_to_complete),
                    modifier = Modifier
                        .fillParentMaxWidth(0.3f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(id = R.string.label_complete_by),
                    modifier = Modifier
                        .fillParentMaxWidth(0.4f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        itemsIndexed(alternateDurations) { index, alternateDuration ->
            DurationRow(alternateDuration, playbacks[index])
            if (index != alternateDurations.lastIndex) {
                Divider()
            }
        }
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
            text = playbackSpeed,
            modifier = Modifier.weight(0.3f),
            textAlign = TextAlign.Center
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

@Preview
@Composable
fun DetailsScreenPreview() {
    Column {
        DurationsList("123412314343", "", "", 123123L)
    }
}