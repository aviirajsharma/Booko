package com.avirajsharma.booko.presentation.screens.detailscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.avirajsharma.booko.data.model.BookDetailResponse
import com.avirajsharma.booko.presentation.screens.ErrorScreen
import com.avirajsharma.booko.presentation.screens.LoadingScreen


@Composable
fun BookDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: BookDetailScreenViewModel = hiltViewModel(),
    bookId: String
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(bookId) {
        viewModel.getBookDetail(bookId)
    }

    Box(Modifier.fillMaxSize()) {
        when (state) {
            is DetailUiState.Loading -> {
                LoadingScreen()
            }

            is DetailUiState.Error -> {
                ErrorScreen(error = (state as DetailUiState.Error).error)
            }

            is DetailUiState.Success -> {
                val response = state as DetailUiState.Success
                DetailBookCard(book = response.data, onDownloadClick = {
                    viewModel.downloadBook(response.data)
                })
            }
        }
    }
}


@Composable
fun DetailBookCard(
    modifier: Modifier = Modifier,
    book: BookDetailResponse,
    onDownloadClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Main Content Area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Header Image Section with Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            ) {
                AsyncImage(
                    model = book.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Bottom shadow for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 600f
                            )
                        )
                )
            }

            // Text Content Section
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .offset(y = (-30).dp) // Pull content up over the image slightly
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 2.dp,
                    shadowElevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = book.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = book.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                        // Quick Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatItem("Pages", book.pages)
                            StatItem("Year", book.year)
                            StatItem("Status", book.status)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Detailed Info
                Text(
                    "About this book",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = book.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp),
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
                )

                DetailRow("Author", book.authors)
                DetailRow("Publisher", book.publisher)

                Spacer(modifier = Modifier.height(100.dp)) // Padding for bottom button
            }
        }

        // Custom Overlay UI: Back Button
        IconButton(
            onClick = {},
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp) // Adjusted for status bar
                .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        // Custom Overlay UI: Action Button
        Button(
            onClick = {
                onDownloadClick()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .fillMaxWidth(0.8f)
                .height(56.dp),
            shape = CircleShape,
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Icon(Icons.Default.Download, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Download PDF")
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            "$label: ",
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodySmall
        )
        Text(value, style = MaterialTheme.typography.bodySmall)
    }
}