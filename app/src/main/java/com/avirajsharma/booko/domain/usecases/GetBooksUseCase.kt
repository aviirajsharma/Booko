package com.avirajsharma.booko.domain.usecases

import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.data.repository.BookRepositoryImpl
import com.avirajsharma.booko.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetBooksUseCase {

    private val repository: BookRepository by lazy {
        BookRepositoryImpl()
    }

    operator fun invoke() = flow<Result<BooksResponse>> {
        try {
            val result = repository.getBooks()
            emit(Result.success(result))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}