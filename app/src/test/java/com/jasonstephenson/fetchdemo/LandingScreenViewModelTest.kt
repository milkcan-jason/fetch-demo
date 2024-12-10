package com.jasonstephenson.fetchdemo

import com.jasonstephenson.fetchdemo.data.FetchData
import com.jasonstephenson.fetchdemo.data.FetchRepository
import com.jasonstephenson.fetchdemo.ui.LandingScreenViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class LandingScreenViewModelTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun filter_isCorrect() = runTest {
        val vm = LandingScreenViewModel(DummyRepo())
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            vm.uiState.collect {}
        }

        // delay waiting on second StateFlow response. Didn't stack flows to Repo for demo purposes.
        while(vm.uiState.value == null) {
            delay(100L)
        }

        vm.uiState.value?.results?.let { results ->
            assert(
                value = results.isNotEmpty(),
                lazyMessage = { "List should not be empty" })
            assert(
                value = results.size == 2,
                lazyMessage = { "Should be 2, are ${vm.uiState.value?.results?.size}" })

            assert(
                value = results.keys.size == 2,
                lazyMessage = { "There should be 2 Groups" }
            )
            assert(
                value = results.containsKey(0),
                lazyMessage = { "There should be a group 0" }
            )
            assert(
                value = results.containsKey(1),
                lazyMessage = { "There should be a group 1" }
            )
            assert(
                value = results[1]?.size == 2,
                lazyMessage = { "Group 1 should have 2 elements" }
            )

            assert(
                value = results[1]?.firstOrNull { it.id == 3 } == null,
                lazyMessage = { "Should not contain id 3" })
        }

        job.cancel()
    }

    @Test
    fun filter_stringOrder() = runTest {
        val vm = LandingScreenViewModel(StringRepo())
        val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            vm.uiState.collect {}
        }

        // delay waiting on second StateFlow response. Didn't stack flows to Repo for demo purposes.
        while (vm.uiState.value == null) {
            delay(100L)
        }

        vm.uiState.value?.results?.let { results ->
            assert(
                value = results[1]?.get(0)?.name == "Test 1",
                lazyMessage = { "Test 1 should be First" })
            assert(
                value = results[1]?.get(2)?.name == "Test 120",
                lazyMessage = { "Test 120 should be Last" })
        }

        job.cancel()
    }

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

    class StringRepo: FetchRepository() {
        override suspend fun loadData(): List<FetchData>  = withContext(Dispatchers.Main) {
            val data: MutableList<FetchData> = mutableListOf()

            // This order is used to verify we order them properly for display
            data.add(FetchData(id = 1, listId = 1, name = "Test 120"))
            data.add(FetchData(id = 1, listId = 1, name = "Test 12"))
            data.add(FetchData(id = 2, listId = 1, name = "Test 1"))

            return@withContext data
        }
    }
}