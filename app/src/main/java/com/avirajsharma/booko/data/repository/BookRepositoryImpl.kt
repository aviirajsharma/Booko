package com.avirajsharma.booko.data.repository

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.avirajsharma.booko.data.local.BookDao
import com.avirajsharma.booko.data.local.BookEntity
import com.avirajsharma.booko.data.model.BookDetailResponse
import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.data.remote.ApiService
import com.avirajsharma.booko.domain.repository.BookRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class BookRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val bookDao: BookDao,
    @ApplicationContext private val context: Context
) : BookRepository {

    override suspend fun getBooks(): BooksResponse = apiService.getBooks()

    override suspend fun searchBooks(query: String): BooksResponse = apiService.searchBook(query)

    override suspend fun getBookDetail(bookId: String): BookDetailResponse =
        apiService.getBookDetail(bookId)


    override suspend fun downloadBook(book: BookDetailResponse) {
        try {
            // Sanitize filename to ensure OS compatibility (removes special characters from titles)
            val sanitizedTitle = book.title.replace(Regex("[^a-zA-Z0-9]"), "_")
            val fileName = "${sanitizedTitle}_${book.id}.pdf"
            val downloadDir = context.getExternalFilesDir("books") ?: return

            val request = DownloadManager.Request(book.download.toUri())
                .setTitle(book.title)
                .setDescription("Downloading Book...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setMimeType("application/pdf")
                // User-Agent spoofing to bypass potential dbooks.org bot protection
                .addRequestHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
                )
                .setDestinationInExternalFilesDir(context, "books", fileName)

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadId = downloadManager.enqueue(request)

            val filePath = File(downloadDir, fileName).absolutePath

            // Save metadata immediately; isDownloaded will be updated via DownloadCompletedReceiver
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
            Toast.makeText(context, "Download started...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getAllBooks(): Flow<List<BookEntity>> = bookDao.getAllBooks()

    override fun openPdf(context: Context, filePath: String) {
        val file = File(filePath)

        if (!file.exists()) {
            Toast.makeText(context, "Book not found", Toast.LENGTH_SHORT).show()
            return
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No PDF viewer found", Toast.LENGTH_SHORT).show()
        }
    }

    override suspend fun deleteBook(book: BookEntity) {
        //first removing from room
        bookDao.deleteBook(bookId = book.id)
        //and then from storage
        val file = File(book.filePath)
        if (file.exists()) {
            file.delete()
        }
    }
}