package com.lndmg.search

import app.cash.turbine.test
import com.lndmg.domain.entity.Location
import com.lndmg.domain.entity.PermissionStatus
import com.lndmg.domain.usecase.*
import com.lndmg.domain.util.Result
import com.lndmg.ui.LocationUi
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val selectLocationUseCase: SelectLocationUseCase = mockk(relaxed = true)
    private val getCurrentLocationUseCase: GetCurentLocationUseCase = mockk()
    private val searchCityUseCase: SearchCityByNameUseCase = mockk()
    private val getLastFiveLocationsUseCase: GetLastFiveLocationsUseCase = mockk()
    private val checkPermissionUseCase: CheckLocationPermissionUseCase = mockk()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getLastFiveLocationsUseCase() } returns flowOf(Result.Success(emptyList()))
        every { checkPermissionUseCase() } returns PermissionStatus.Denied
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load last five locations`() = runTest {
        val mockLocations = listOf(Location("New York", longName = "New York", lat = 12.2, lng = 12.2, timeStamp = 0, isUserLocation = false))
        every { getLastFiveLocationsUseCase() } returns flowOf(Result.Success(mockLocations))

        initViewModel()
        runCurrent()
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(1, state.savedCities.size)
            assertEquals("New York", state.savedCities[0].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchQueryChanged should trigger search after debounce`() = runTest {
        initViewModel()
        val mockResults = listOf(Location("London", longName = "London", lat = 12.2, lng = 12.2, timeStamp = 0, isUserLocation = false))
        every { searchCityUseCase("Lon") } returns flowOf(Result.Success(mockResults))
        viewModel.onSearchQueryChanged("Lon")
        assertEquals(0, viewModel.state.value.searchedCities.size)
        advanceTimeBy(301)

        viewModel.state.test {
            val state = awaitItem()
            assertEquals(1, state.searchedCities.size)
            assertEquals("London", state.searchedCities[0].name)
        }
    }

    @Test
    fun `onPermissionResult Granted should update state and fetch location`() = runTest {

        val mockLocation = Location("Paris", longName = "Paris", lat = 12.2, lng = 12.2, timeStamp = 0, isUserLocation = true)
        every { getCurrentLocationUseCase() } returns flowOf(Result.Success(mockLocation))
        initViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            viewModel.onPermissionResult(isGranted = true, isRationaleNeeded = false)
            val permissionState = awaitItem()
            assertEquals(LocationPermissionState.Granted, permissionState.permissionState)
            val cityState = awaitItem()
            assertEquals("Paris", cityState.currentCity?.name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCitySelected should clear search query`() = runTest {
        initViewModel()
        val city = LocationUi("Tokyo", longName = "Tokyo", lat = 12.2, lng = 12.2, timeStamp = 0, isUserLocation = false)
        viewModel.onCitySelected(city)
        assertEquals("", viewModel.state.value.searchQuery)
    }

    private fun initViewModel() {
        viewModel = SearchViewModel(
            selectLocationUseCase,
            getCurrentLocationUseCase,
            searchCityUseCase,
            getLastFiveLocationsUseCase,
            checkPermissionUseCase
        )
    }
}