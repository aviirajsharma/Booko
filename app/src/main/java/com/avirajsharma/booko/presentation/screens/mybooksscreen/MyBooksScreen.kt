package com.avirajsharma.booko.presentation.screens.mybooksscreen

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.avirajsharma.booko.data.local.BookEntity
import com.avirajsharma.booko.presentation.components.EmptyState
import com.avirajsharma.booko.presentation.screens.ErrorScreen
import com.avirajsharma.booko.presentation.screens.LoadingScreen

@Composable
fun MyBooksScreen(
    modifier: Modifier = Modifier,
    viewModel: MyBooksScreenViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        is MyBooksState.Loading -> {
            LoadingScreen()
        }

        is MyBooksState.Error -> {
            ErrorScreen(error = (state as MyBooksState.Error).error)
        }

        is MyBooksState.Success -> {
            val data = (state as MyBooksState.Success).data
            if (data.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.MenuBook,
                    title = "No Books Downloaded",
                    description = "Your downloaded books will appear here."
                )
            } else {
                LazyColumn {
                    items(data) { book ->
                        MyBooksCard(book = book, openPdf = { context, filePath ->
                            viewModel.openPdf(context, filePath)
                        }, deleteBook = { bookId ->
                            viewModel.deleteBook(bookId)
                        })
                    }
                }
            }
        }
    }
}


@Composable
fun MyBooksCard(
    modifier: Modifier = Modifier,
    book: BookEntity,
    openPdf: (Context, String) -> Unit,
    deleteBook: (String) -> Unit
) {
    val context = LocalContext.current
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = {
                openPdf(context, book.filePath)
            })
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = book.image,
                contentDescription = "Cover of ${book.title}",
                modifier = Modifier
                    .size(width = 100.dp, height = 150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = book.authors,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = if (book.isDownloaded) "Downloaded" else "Downloading...",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (book.isDownloaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )

                IconButton(onClick = {
                    deleteBook(book.id)
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Book")
                }
            }
        }
    }
}