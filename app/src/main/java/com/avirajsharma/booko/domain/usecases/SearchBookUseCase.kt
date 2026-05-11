package com.avirajsharma.booko.domain.usecases

import com.avirajsharma.booko.data.model.BooksResponse
import com.avirajsharma.booko.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SearchBookUseCase @Inject constructor(private val repository: BookRepository) {


    operator fun invoke(query: String) = flow<Result<BooksResponse>> {
        val response = repository.searchBooks(query)
        emit(Result.success(response))
    }.catch {
        emit(Result.failure(it))
    }.flowOn(Dispatchers.IO)

}