package com.avirajsharma.booko.di

import android.content.Context
import androidx.room.Room
import com.avirajsharma.booko.data.local.BookDao
import com.avirajsharma.booko.data.local.BookoDatabase
import com.avirajsharma.booko.data.remote.ApiService
import com.avirajsharma.booko.domain.repository.BookRepository
import com.avirajsharma.booko.domain.usecases.DownloadBookUseCase
import com.avirajsharma.booko.domain.usecases.GetAllBooksUseCase
import com.avirajsharma.booko.domain.usecases.GetBookDetailUseCase
import com.avirajsharma.booko.domain.usecases.GetBooksUseCase
import com.avirajsharma.booko.domain.usecases.SearchBookUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideBookApi(client: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookoDatabase {
        return Room.databaseBuilder(
            context,
            klass = BookoDatabase::class.java,
            name = "booko_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideBookDao(database: BookoDatabase): BookDao {
        return database.bookDao()
    }

    @Provides
    fun provideGetBookUseCase(repository: BookRepository): GetBooksUseCase {
        return GetBooksUseCase(repository)
    }

    @Provides
    fun provideSearchBookUseCase(repository: BookRepository): SearchBookUseCase {
        return SearchBookUseCase(repository)
    }

    @Provides
    fun provideGetBookDetailUseCase(repository: BookRepository): GetBookDetailUseCase {
        return GetBookDetailUseCase(repository)
    }

    @Provides
    fun provideDownloadBookUseCase(repository: BookRepository): DownloadBookUseCase {
        return DownloadBookUseCase(repository)
    }

    @Provides
    fun provideGetAllBooksUseCase(repository: BookRepository): GetAllBooksUseCase {
        return GetAllBooksUseCase(repository)
    }


}