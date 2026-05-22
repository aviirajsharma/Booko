package com.avirajsharma.booko.domain.usecases

import com.avirajsharma.booko.data.local.BookEntity
import com.avirajsharma.booko.domain.repository.BookRepository
import javax.inject.Inject

class DeleteBookUseCase @Inject constructor(private val repository: BookRepository) {

    suspend operator fun invoke(book: BookEntity) {
        repository.deleteBook(book)
    }
}