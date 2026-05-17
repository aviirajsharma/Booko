package com.avirajsharma.booko.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBook(bookId: String)

    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): BookEntity?

    @Query("SELECT * FROM books WHERE downloadId = :downloadId")
    suspend fun getBookByDownloadId(downloadId: Long): BookEntity?

    @Query("UPDATE books SET isDownloaded = :isDownloaded WHERE downloadId = :downloadId")
    suspend fun updateDownloadStatus(downloadId: Long, isDownloaded: Boolean)
}