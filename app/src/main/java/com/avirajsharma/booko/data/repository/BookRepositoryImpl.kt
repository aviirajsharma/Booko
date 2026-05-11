package com.avirajsharma.booko.data.repository

import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.data.remote.ApiService
import com.avirajsharma.booko.domain.repository.BookRepository
import jakarta.inject.Inject

class BookRepositoryImpl @Inject constructor(private val apiService: ApiService) : BookRepository {

    override suspend fun getBooks(): BooksResponse {
        return apiService.getBooks()
    }

    override suspend fun searchBooks(query: String): BooksResponse {
        return apiService.searchBook(query = query)
    }
}