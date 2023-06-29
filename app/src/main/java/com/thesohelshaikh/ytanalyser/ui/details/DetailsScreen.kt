package com.thesohelshaikh.ytanalyser.ui.details

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thesohelshaikh.ytanalyser.R
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thesohelshaikh.ytanalyser.UtilitiesManger
import java.util.ArrayList
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
            Column {
                Details(
                    successState.thumbnailUrl,
                    successState.title,
                    successState.channelTitle,
                    successState.duration
                )
            }
        }

        null -> {

        }
    }

}

fun observeState() {

}

@Composable
private fun Details(
    thumbnailUrl: String?,
    title: String?,
    channelTitle: String?,
    duration: Long
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(thumbnailUrl)
            .crossfade(true)
            .build(), contentDescription = "Thumbnail",
        modifier = Modifier.aspectRatio(16f / 9f),
        contentScale = ContentScale.Crop
    )
    Text(text = title ?: "", style = MaterialTheme.typography.titleLarge)
    Text(text = channelTitle ?: "", style = MaterialTheme.typography.bodyMedium)
    Text(
        text = UtilitiesManger.getPrettyDuration(duration),
        style = MaterialTheme.typography.bodyMedium
    )

    val alternateDurations = UtilitiesManger.calculateAlternateDurations(Date(duration))
    val playbacks = mutableListOf<String>()
    playbacks.add("At 1x")
    playbacks.add("At 1.25x")
    playbacks.add("At 1.5x")
    playbacks.add("At 1.75x")
    playbacks.add("At 2x")

    LazyColumn {
        itemsIndexed(alternateDurations) {  index, alternateDuration ->
            DurationRow(alternateDuration, playbacks[index])
        }
    }
}

@Composable
private fun DurationRow(alternateDuration: Long, playbackSpeed: String) {
    Text(text = playbackSpeed)
    Text(text = "To complete ${UtilitiesManger.getPrettyDuration(alternateDuration)}")
    Text(text = "Complete by ${UtilitiesManger.getDateAfter(alternateDuration)}")
}

@Preview
@Composable
fun DetailsScreenPreview() {
    Column() {
        Details("123412314343", "", "", 123123L)
    }
}