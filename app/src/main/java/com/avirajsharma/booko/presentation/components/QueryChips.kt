package com.avirajsharma.booko.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QueryChipsRow(
    topics: List<String>,
    selectedTopic: String?,
    modifier: Modifier = Modifier,
    onTopicSelected: (String) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(topics) { topic ->
            FilterChip(
                selected = selectedTopic == topic,
                onClick = {
                    onTopicSelected(topic)
                },
                label = {
                    Text(text = topic)
                }
            )
        }
    }
}
