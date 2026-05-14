package com.avirajsharma.booko.data.repository

import android.app.DownloadManager
import android.content.Context
import androidx.core.net.toUri
import com.avirajsharma.booko.data.model.BookDetailResponse
import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.data.remote.ApiService
import com.avirajsharma.booko.domain.repository.BookRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) : BookRepository {

    override suspend fun getBooks(): BooksResponse {
        return apiService.getBooks()
    }

    override suspend fun searchBooks(query: String): BooksResponse {
        return apiService.searchBook(query = query)
    }

    override suspend fun getBookDetail(bookId: String): BookDetailResponse {
        return apiService.getBookDetail(bookId)
    }

    override  fun downloadBook(book: BookDetailResponse) {
        val request = DownloadManager.Request(
            book.download.toUri()
        )
            .setTitle(book.title)
            .setDescription("Downloading Book...")
            .setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setMimeType("application/pdf")
            .setDestinationInExternalFilesDir(
                context,
                "books",
                "${book.title}.pdf"
            )

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        downloadManager.enqueue(request)
    }

}