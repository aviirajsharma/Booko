package com.avirajsharma.booko.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.avirajsharma.booko.presentation.components.BookCard
import com.avirajsharma.booko.presentation.viewmodels.HomeScreenViewModel


@Composable
fun HomeScreen(modifier: Modifier, viewModel: HomeScreenViewModel) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isLoading) {
        LoadingScreen(modifier)
    }
    if (state.error!!.isNotEmpty()) {
        ErrorScreen(modifier, error = state.error.toString())
    }

    state.data?.let { data ->
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(data.books) { book ->
                BookCard(book = book)
            }
        }
    }

}