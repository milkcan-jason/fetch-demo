package com.jasonstephenson.fetchdemo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    ) {
        Text(
            text = "Fetch Data:",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding()
        )
        LazyColumn(horizontalAlignment = Alignment.Start) {
            data?.results?.let { results ->
            for(key in results.keys) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "ListId: $key",
                                fontSize = 24.sp,
                            )
                            for (fetchData in results.getValue(key)) {
                                Text(
                                    text = "Id: ${fetchData.id} - Name: ${fetchData.name}",
                                    fontSize = 16.sp,
                                )
                            }
                        }
                    }
                }
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
