package com.jasonstephenson.fetchdemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasonstephenson.fetchdemo.data.FetchData
import com.jasonstephenson.fetchdemo.data.FetchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LandingScreenViewModel(
    private val repository: FetchRepository = FetchRepository()
): ViewModel() {
    private val _uiState: MutableStateFlow<UiState?> = MutableStateFlow(null)
    val uiState: StateFlow<UiState?> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = repository.loadData()

            // get rid of null/empty names
            // then sort by listId, then name
            val sortedList = result.filter {
                !it.name.isNullOrEmpty()
            }.sortedWith(compareBy({it.listId}, {it.name} ))

            // group the sorted list into groups by listId
            val grouped = sortedList.groupBy { selector ->
                selector.listId
            }

            _uiState.update {
               UiState(results = grouped)
            }
        }
    }

    data class UiState(val results: Map<Int, List<FetchData>> = mapOf())
}