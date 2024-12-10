package com.jasonstephenson.fetchdemo.data

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FetchApiClient {
    private val URL = "https://fetch-hiring.s3.amazonaws.com/"
    private val gson = GsonBuilder().create()

    private fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val fetchService: FetchApi by lazy {
        retrofit().create(FetchApi::class.java)
    }
}