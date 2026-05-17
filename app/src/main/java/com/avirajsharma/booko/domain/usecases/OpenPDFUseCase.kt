package com.avirajsharma.booko.domain.usecases

import android.content.Context
import com.avirajsharma.booko.domain.repository.BookRepository
import javax.inject.Inject

class OpenPDFUseCase @Inject constructor(private val bookRepository: BookRepository) {

    operator fun invoke(context: Context, filePath: String) {
        return bookRepository.openPdf(context, filePath)
    }
}