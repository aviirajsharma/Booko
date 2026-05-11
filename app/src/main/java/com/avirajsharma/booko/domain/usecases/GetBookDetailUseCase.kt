package com.avirajsharma.booko.domain.usecases

import com.avirajsharma.booko.data.model.BookDetailResponse
import com.avirajsharma.booko.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBookDetailUseCase @Inject constructor(private val repository: BookRepository) {

    operator fun invoke(bookId: String) = flow<Result<BookDetailResponse>> {
        try {
            val result = repository.getBookDetail(bookId)
            emit(Result.success(result))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

}