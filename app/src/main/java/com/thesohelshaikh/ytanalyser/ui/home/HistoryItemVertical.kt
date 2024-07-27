package com.thesohelshaikh.ytanalyser.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.data.model.HistoryItem
import com.thesohelshaikh.ytanalyser.data.model.ResourceType

@Composable
fun HistoryItemVertical(
    videoEntity: HistoryItem,
    onItemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable {
                onItemClick(videoEntity.id)
            }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .width(IntrinsicSize.Min)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(videoEntity.thumbnail)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(id = R.string.cd_thumbnail),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(90.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(R.drawable.ic_launcher_background),
            )
            Text(
                text = videoEntity.title,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewHistoryItemVertical() {
    HistoryItemVertical(
        videoEntity = HistoryItem(
            "1231231",
            null,
            "Video Title very very very very big indeed",
            "Channel title",
            ResourceType.VIDEO,
            123456L,
        ),
        onItemClick = {}
    )
}