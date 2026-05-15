package com.avirajsharma.booko.data.local

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [BookEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BookoDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}