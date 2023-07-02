package com.thesohelshaikh.ytanalyser.ui.details

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thesohelshaikh.ytanalyser.UtilitiesManger
import java.util.Date

@Composable
fun DetailsScreen(
    videoId: String,
    informationViewModel: InformationViewModel = viewModel(factory = InformationViewModel.Factory)
) {
    LaunchedEffect(key1 = Unit, block = {
        informationViewModel.getVideoDetails(videoId)
    })

    val state by informationViewModel.detailsScreenState.observeAsState()

    when (state) {
        is InformationViewModel.DetailsScreenState.ErrorState -> {

        }

        InformationViewModel.DetailsScreenState.LoadingState -> {
            Log.d("TAG", "DetailsScreen: $state")
        }

        is InformationViewModel.DetailsScreenState.SuccessState -> {
            val successState = state as InformationViewModel.DetailsScreenState.SuccessState
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
private fun DurationsList(
    thumbnailUrl: String?,
    title: String?,
    channelTitle: String?,
    duration: Long
) {
    val alternateDurations = UtilitiesManger.calculateAlternateDurations(Date(duration))
    val playbacks = mutableListOf<String>()
    playbacks.add("1x")
    playbacks.add("1.25x")
    playbacks.add("1.5x")
    playbacks.add("1.75x")
    playbacks.add("2x")

    LazyColumn {
        item {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .crossfade(true)
                    .build(), contentDescription = "Thumbnail",
                modifier = Modifier.aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title ?: "", style = MaterialTheme.typography.titleLarge)
            Text(text = channelTitle ?: "", style = MaterialTheme.typography.titleMedium)
            Text(
                text = UtilitiesManger.getPrettyDuration(duration),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Playback\nSpeed",
                    modifier = Modifier
                        .fillParentMaxWidth(0.3f)
                )
                Text(
                    text = "To complete", modifier = Modifier
                        .fillParentMaxWidth(0.3f)
                )
                Text(
                    text = "Complete by", modifier = Modifier
                        .fillParentMaxWidth(0.4f)
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
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = playbackSpeed, modifier = Modifier.weight(0.3f))
        Text(
            text = UtilitiesManger.getPrettyDuration(alternateDuration),
            modifier = Modifier.weight(0.3f)
        )
        Text(
            text = UtilitiesManger.getDateAfter(alternateDuration),
            modifier = Modifier.weight(0.4f)
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