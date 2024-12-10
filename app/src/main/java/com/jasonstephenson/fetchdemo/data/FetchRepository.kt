package com.jasonstephenson.fetchdemo.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class FetchRepository {
    open suspend fun loadData(): List<FetchData> = withContext(Dispatchers.IO) {
        val client = FetchApiClient().fetchService

        return@withContext client.getData()
    }
}