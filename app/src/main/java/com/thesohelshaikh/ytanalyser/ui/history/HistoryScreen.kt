package com.thesohelshaikh.ytanalyser.ui.history

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thesohelshaikh.ytanalyser.R


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    onVideoClick: (String) -> Unit,
    historyViewModel: HistoryViewModel = viewModel(factory = HistoryViewModel.Factory)
) {

    LaunchedEffect(key1 = Unit) {
        historyViewModel.getVideosAndPlaylists()
    }
    val screenState = historyViewModel.historyScreenState.observeAsState()
    var selectedFilter by remember { mutableStateOf(FilterType.ALL) }

    when (screenState.value) {
        is HistoryViewModel.HistoryUiState.Success -> {
            val videos = (screenState.value as HistoryViewModel.HistoryUiState.Success).videos


            LazyColumn() {
                stickyHeader {
                    FilterRow(selectedFilter) { type ->
                        Log.d("TAG", "HistoryScreen: filter")
                        selectedFilter = type
                        when (type) {
                            FilterType.ALL -> historyViewModel.getVideosAndPlaylists()
                            FilterType.VIDEO -> historyViewModel.getVideos()
                            FilterType.PLAYLIST -> historyViewModel.getPlaylists()
                        }
                    }
                }
                items(videos) {
                    HistoryItemRow(it, onVideoClick)
                }
            }

        }

        null -> {
            /* no-op */
        }
    }

}

enum class FilterType {
    ALL, VIDEO, PLAYLIST
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FilterRow(selectedFilter: FilterType, onFilterSelected: (FilterType) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.Start,
    ) {
        FilterChip(
            selected = selectedFilter == FilterType.ALL,
            onClick = {
                onFilterSelected(FilterType.ALL)
            },
            label = {
                Text(text = "All")
            },
            modifier = Modifier.padding(start = 16.dp)
        )
        FilterChip(
            selected = selectedFilter == FilterType.VIDEO,
            onClick = {
                onFilterSelected(FilterType.VIDEO)
            },
            label = {
                Text(text = "Videos")
            },
            modifier = Modifier.padding(start = 16.dp),
        )
        FilterChip(
            selected = selectedFilter == FilterType.PLAYLIST,
            onClick = {
                onFilterSelected(FilterType.PLAYLIST)
            },
            label = {
                Text(text = "Playlists")
            },
            modifier = Modifier.padding(start = 16.dp),
        )

    }
}

@Composable
fun HistoryItemRow(videoEntity: HistoryViewModel.HistoryItem, onVideoClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable {
                onVideoClick(videoEntity.id)
            }
    ) {
        Box(
            Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Row(modifier = Modifier) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(videoEntity.thumbnail)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Thumbnail",
                    modifier = Modifier
                        .align(Alignment.Top)
                        .height(90.dp)
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.Top)
                ) {
                    Text(
                        text = videoEntity.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = videoEntity.channelTitle,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}
