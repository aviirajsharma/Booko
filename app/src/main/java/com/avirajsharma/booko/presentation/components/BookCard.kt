package com.avirajsharma.booko.presentation.components

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import com.avirajsharma.booko.data.model.Book


@Composable
fun BookCard(book: Book) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(book.title)
            Spacer(modifier = Modifier.height(10.dp))
            Text(book.authors)
            Spacer(modifier = Modifier.height(10.dp))
            Text(book.subtitle)
            Spacer(modifier = Modifier.height(10.dp))
            AsyncImage(model = book.image, contentDescription = null)
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = book.url.toUri()
                context.startActivity(intent)
            }) {
                Text("Download")
            }
        }
    }
}