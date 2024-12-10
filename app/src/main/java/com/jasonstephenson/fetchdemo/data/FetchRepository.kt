package com.jasonstephenson.fetchdemo.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class FetchRepository {
    open suspend fun loadData(): List<FetchData> = withContext(Dispatchers.IO) {
        // dummy data to wire up infrastructure
        val data: MutableList<FetchData> = mutableListOf()
        for(i in 1..5) {
            val name = if(i%3 == 0) null else "Name $i"
            val itr = FetchData(id = i, listId = i%2, name = name)
            data.add(itr)
        }

        return@withContext data
    }
}