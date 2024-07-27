package com.thesohelshaikh.ytanalyser.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.data.model.HistoryItem
import com.thesohelshaikh.ytanalyser.ui.history.HistoryEmptyState

@Composable
fun HistorySection(
    onViewHistory: () -> Unit,
    historyItems: List<HistoryItem>,
    onHistoryItemClick: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.screen_history),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1f)
        )
        IconButton(onClick = {
            onViewHistory()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }

    if (historyItems.isEmpty()) {
        HistoryEmptyState()
    } else {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(historyItems) { historyItem ->
                HistoryItemVertical(
                    videoEntity = historyItem,
                    onHistoryItemClick
                )
            }
        }
    }

}