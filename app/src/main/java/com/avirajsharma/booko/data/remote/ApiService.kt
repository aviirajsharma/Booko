package com.avirajsharma.booko.data.remote

import com.avirajsharma.booko.data.model.BooksResponse
import retrofit2.http.GET

interface ApiService {

    //https://www.dbooks.org/api/recent

    @GET("recent")
    suspend fun getBooks(): BooksResponse
}