package com.jasonstephenson.fetchdemo.data

import retrofit2.http.GET

interface FetchApi {
    @GET("hiring.json")
    suspend fun getData() : List<FetchData>
}