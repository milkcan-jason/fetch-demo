package com.jasonstephenson.fetchdemo.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchRepository {
    suspend fun loadData(): List<FetchData> = withContext(Dispatchers.IO) {
        // dummy data to wire up infrastructure
        val data: MutableList<FetchData> = mutableListOf()
        for(i in 1..5) {
            val itr = FetchData(id = i, listId = 20 + i, name = "$i")
            data.add(itr)
        }

        return@withContext data
    }
}