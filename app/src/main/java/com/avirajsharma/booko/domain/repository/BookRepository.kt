package com.avirajsharma.booko.domain.repository

import com.avirajsharma.booko.data.model.BookDetailResponse
import com.avirajsharma.booko.data.model.BooksResponse

interface BookRepository {
    suspend fun getBooks(): BooksResponse

    suspend fun searchBooks(query: String): BooksResponse


    suspend fun getBookDetail(bookId: String): BookDetailResponse

    fun downloadBook(book: BookDetailResponse)
}