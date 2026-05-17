package com.avirajsharma.booko.domain.usecases

import com.avirajsharma.booko.domain.repository.BookRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(private val repository: BookRepository) {

    suspend operator fun invoke(bookId : String) {
        repository.deleteBook(bookId)
    }
}