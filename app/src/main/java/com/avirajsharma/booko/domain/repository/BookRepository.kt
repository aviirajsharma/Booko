package com.avirajsharma.booko.domain.repository

import com.avirajsharma.booko.data.model.BooksResponse

interface BookRepository {
    suspend fun getBooks(): BooksResponse
}