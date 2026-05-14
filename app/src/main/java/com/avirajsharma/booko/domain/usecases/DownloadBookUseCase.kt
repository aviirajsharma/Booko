package com.avirajsharma.booko.domain.usecases

import com.avirajsharma.booko.data.model.BookDetailResponse
import com.avirajsharma.booko.domain.repository.BookRepository
import javax.inject.Inject

class DownloadBookUseCase @Inject constructor(private val repository: BookRepository) {

    operator fun invoke(book: BookDetailResponse) {

        repository.downloadBook(book)
    }

}