package com.avirajsharma.booko.domain.repository

import com.avirajsharma.booko.data.local.BookEntity
import com.avirajsharma.booko.data.model.BookDetailResponse
import com.avirajsharma.booko.data.model.BooksResponse
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun getBooks(): BooksResponse

    suspend fun searchBooks(query: String): BooksResponse


    suspend fun getBookDetail(bookId: String): BookDetailResponse

    suspend fun downloadBook(book: BookDetailResponse)

    fun getAllBooks(): Flow<List<BookEntity>>

}