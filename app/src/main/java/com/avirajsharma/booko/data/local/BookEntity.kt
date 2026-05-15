package com.avirajsharma.booko.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val id: String,
    val image: String,
    val title: String,
    val authors: String,
    val filePath: String,
    val downloadId: Long,
    val isDownloaded: Boolean
)
