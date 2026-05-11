package com.avirajsharma.booko.data.remote

import com.avirajsharma.booko.data.model.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    //Recents
    //https://www.dbooks.org/api/recent

    //Search Books
    //https://www.dbooks.org/api/search/{query}

    @GET("recent")
    suspend fun getBooks(): BooksResponse

    @GET("search/{query}")
    suspend fun searchBook(
        @Path("query") query: String
    ): BooksResponse

    companion object{
        const val BASE_URL = "https://www.dbooks.org/api/"
    }
}