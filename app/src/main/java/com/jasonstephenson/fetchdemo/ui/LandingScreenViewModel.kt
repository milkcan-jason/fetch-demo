package com.jasonstephenson.fetchdemo.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasonstephenson.fetchdemo.data.FetchData
import com.jasonstephenson.fetchdemo.data.FetchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LandingScreenViewModel: ViewModel() {
    private val repository = FetchRepository()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = repository.loadData()

            _uiState.update {
               UiState(results = result)
            }
        }
    }

    data class UiState(val results: List<FetchData> = listOf())
}