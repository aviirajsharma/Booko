package com.avirajsharma.booko.data.model

data class BooksResponse(
    val books: List<Book>? = emptyList(),
    val status: String,
    val total: Int
)