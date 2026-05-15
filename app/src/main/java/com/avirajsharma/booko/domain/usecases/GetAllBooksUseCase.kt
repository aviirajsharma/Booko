package com.avirajsharma.booko.domain.usecases

import com.avirajsharma.booko.data.local.BookEntity
import com.avirajsharma.booko.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllBooksUseCase @Inject constructor(private val bookRepository: BookRepository) {

    operator fun invoke(): Flow<Result<List<BookEntity>>> = bookRepository.getAllBooks()
        .map { Result.success(it) }
        .catch { emit(Result.failure(it)) }
        .flowOn(Dispatchers.IO)
}