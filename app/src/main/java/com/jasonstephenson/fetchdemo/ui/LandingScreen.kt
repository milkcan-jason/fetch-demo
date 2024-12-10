package com.jasonstephenson.fetchdemo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jasonstephenson.fetchdemo.data.FetchData
import com.jasonstephenson.fetchdemo.data.FetchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
                                fontWeight = FontWeight.Bold,
                            )
                            for (fetchData in results.getValue(key)) {
                                Row {
                                    Text(
                                        text = "Id: ${fetchData.id}",
                                        fontSize = 20.sp,
                                        modifier = Modifier.weight(.2f)
                                    )
                                    Text(
                                        text = "Name: ${fetchData.name}",
                                        fontSize = 20.sp,
                                        modifier = Modifier.weight(.5f)
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
}


@Preview
@Composable
fun LandingScreen_Preview() {
    class DummyRepo: FetchRepository() {
        override suspend fun loadData(): List<FetchData>  = withContext(Dispatchers.Main) {
            // dummy data to wire up infrastructure
            val data: MutableList<FetchData> = mutableListOf()
            // Count down to verify order
            for(i in 5 downTo 1) {
                // set #3 to a null name
                val name = if(i%3 == 0) null else "Name $i"
                // create data points
                val itr = FetchData(id = i, listId = i%2, name = name)
                data.add(itr)
            }

            return@withContext data
        }
    }

    LandingScreen(
        modifier = Modifier.background(Color.White),
        viewModel = LandingScreenViewModel(DummyRepo())
    )
}
