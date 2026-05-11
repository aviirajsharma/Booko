package com.avirajsharma.booko.domain.usecases

import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(private val repository: BookRepository) {

    operator fun invoke() = flow<Result<BooksResponse>> {
        try {
            val result = repository.getBooks()
            emit(Result.success(result))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}