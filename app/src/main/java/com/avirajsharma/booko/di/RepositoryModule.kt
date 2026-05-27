package com.avirajsharma.booko.di

import com.avirajsharma.booko.data.repository.BookRepositoryImpl
import com.avirajsharma.booko.data.repository.SettingsRepositoryImpl
import com.avirajsharma.booko.domain.repository.BookRepository
import com.avirajsharma.booko.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBookRepository(
        impl: BookRepositoryImpl
    ) : BookRepository

    @Binds
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ) : SettingsRepository

}