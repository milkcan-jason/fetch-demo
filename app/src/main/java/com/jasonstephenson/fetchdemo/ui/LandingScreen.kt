package com.jasonstephenson.fetchdemo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    viewModel: LandingScreenViewModel = LandingScreenViewModel()
) {
    // Note: I'd normally dependency inject the viewModel w/ @HiltViewModel, but didn't set up Hilt
    // for this project


    val data by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Fetch Data:")
        LazyColumn {
            for(item in data.results) {
                item {
                    Text(text = "Id: ${item.id}")
                }
            }
        }
    }
}


@Preview
@Composable
fun LandingScreen_Preview() {
    LandingScreen()
}
