package com.avirajsharma.booko.data.repository

import android.app.DownloadManager
import android.content.Context
import androidx.core.net.toUri
import com.avirajsharma.booko.data.local.BookDao
import com.avirajsharma.booko.data.local.BookEntity
import com.avirajsharma.booko.data.model.BookDetailResponse
import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.data.remote.ApiService
import com.avirajsharma.booko.domain.repository.BookRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.io.File

class BookRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val bookDao: BookDao,
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

    override suspend fun downloadBook(book: BookDetailResponse) {
        try {
            val fileName = "${book.title.replace(" ", "_")}.pdf"
            val downloadDir = context.getExternalFilesDir("books") ?: return

            val request = DownloadManager.Request(book.download.toUri())
                .setTitle(book.title)
                .setDescription("Downloading Book...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setMimeType("application/pdf")
                .setDestinationInExternalFilesDir(context, "books", fileName)

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)

            val filePath = File(downloadDir, fileName).absolutePath

            bookDao.insertBook(
                BookEntity(
                    id = book.id,
                    title = book.title,
                    authors = book.authors,
                    image = book.image,
                    filePath = filePath,
                    downloadId = downloadId,
                    isDownloaded = false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getAllBooks(): Flow<List<BookEntity>> {
        return bookDao.getAllBooks()
    }
}