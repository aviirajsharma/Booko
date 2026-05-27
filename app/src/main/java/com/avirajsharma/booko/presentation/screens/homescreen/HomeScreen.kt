package com.avirajsharma.booko.presentation.screens.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.avirajsharma.booko.presentation.components.BookCard
import com.avirajsharma.booko.presentation.components.QueryChipsRow
import com.avirajsharma.booko.presentation.components.SearchBarPlaceholder
import com.avirajsharma.booko.presentation.screens.ErrorScreen
import com.avirajsharma.booko.presentation.screens.LoadingScreen


@Composable
fun HomeScreen(
    homeViewModel: HomeScreenViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onSearchClicked: () -> Unit,
    onBookCardClick: (String) -> Unit
) {

    val state by homeViewModel.uiState.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        SearchBarPlaceholder(onClick = onSearchClicked)
        var selectedTopic by remember { mutableStateOf<String?>(null) }
        QueryChipsRow(
            topics = BookQueries.queries,
            selectedTopic = selectedTopic,
            onTopicSelected = { topic ->
                selectedTopic = topic
                homeViewModel.searchBooks(topic)
            }
        )
        Spacer(Modifier.height(18.dp))

        when (state) {

            is HomeUiState.Loading -> {
                LoadingScreen()
            }

            is HomeUiState.Error -> {
                ErrorScreen(error = (state as HomeUiState.Error).error)
            }

            is HomeUiState.Success -> {
                val data = (state as HomeUiState.Success).data
                val books = data.books ?: emptyList()

                LazyColumn {
                    items(books) { book ->
                        BookCard(book = book, onBookCardClick = onBookCardClick)
                    }
                }
            }
        }

    }
}