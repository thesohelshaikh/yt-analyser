package com.thesohelshaikh.ytanalyser.ui.history

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.data.model.ResourceType
import com.thesohelshaikh.ytanalyser.ui.theme.AppTheme


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

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
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
                if (videos.isEmpty()) {
                    item {
                        HistoryEmptyState()
                    }
                } else {
                    items(videos) {
                        HistoryItemRow(it, onVideoClick)
                    }
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
                Text(text = stringResource(R.string.label_all))
            },
            modifier = Modifier.padding(start = 16.dp)
        )
        FilterChip(
            selected = selectedFilter == FilterType.PLAYLIST,
            onClick = {
                onFilterSelected(FilterType.PLAYLIST)
            },
            label = {
                Text(text = stringResource(R.string.label_playlists))
            },
            modifier = Modifier.padding(start = 16.dp),
        )
        FilterChip(
            selected = selectedFilter == FilterType.VIDEO,
            onClick = {
                onFilterSelected(FilterType.VIDEO)
            },
            label = {
                Text(text = stringResource(R.string.label_videos))
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
            .fillMaxWidth()
            .clickable {
                onVideoClick(videoEntity.id)
            }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(videoEntity.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.cd_thumbnail),
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

@Preview
@Composable
fun FilterRowPreview() {
    AppTheme {
        FilterRow(selectedFilter = FilterType.ALL, onFilterSelected = {})
    }
}

@Preview
@Composable
fun HistoryItemPreview() {
    HistoryItemRow(
        videoEntity = HistoryViewModel.HistoryItem(
            "1231231",
            null,
            "Video Title",
            "Channel title",
            ResourceType.VIDEO,
            123456L
        ),
        onVideoClick = {}
    )
}
