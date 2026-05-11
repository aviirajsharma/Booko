package com.avirajsharma.booko.presentation.screens.searchscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.avirajsharma.booko.presentation.components.BookCard
import com.avirajsharma.booko.presentation.components.EmptyState
import com.avirajsharma.booko.presentation.screens.ErrorScreen
import com.avirajsharma.booko.presentation.screens.LoadingScreen


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchScreenViewModel = hiltViewModel(),
    onBookCardClick: (String) -> Unit
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    var query by remember { mutableStateOf("") }
    Column(modifier.fillMaxSize()) {
        TextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.searchQuery(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search Books...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        when (state) {

            is SearchUiState.Idle -> {
                EmptyState(
                    icon = Icons.Default.Search,
                    title = "Start searching",
                    description = "Find the best books to read for free"
                )
            }

            is SearchUiState.Loading -> {
                LoadingScreen()
            }

            is SearchUiState.Error -> {
                ErrorScreen(error = (state as SearchUiState.Error).error)
            }

            is SearchUiState.Success -> {

                val data = (state as SearchUiState.Success).result
                val books = data.books ?: emptyList()

                if (books.isEmpty()) {
                    EmptyState(
                        icon = Icons.Default.Search,
                        title = "No results found",
                        description = "Try searching for something else"
                    )
                } else {
                    LazyColumn {
                        items(books) { book ->
                            BookCard(book = book, onBookCardClick = onBookCardClick)
                        }
                    }
                }
            }
        }
    }
}