package com.lndmg.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lndmg.domain.entity.Location
import com.lndmg.domain.entity.PermissionStatus
import com.lndmg.domain.usecase.CheckLocationPermissionUseCase
import com.lndmg.domain.usecase.GetCurentLocationUseCase
import com.lndmg.domain.usecase.GetLastFiveLocationsUseCase
import com.lndmg.domain.usecase.SearchCityByNameUseCase
import com.lndmg.domain.usecase.SelectLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import com.lndmg.domain.util.Result
import com.lndmg.ui.LocationUi
import com.lndmg.ui.toDomain
import com.lndmg.ui.toUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val useCaseSelectLocation: SelectLocationUseCase,
    private val useCaseGetCurentLocation: GetCurentLocationUseCase,
    private val useCaseSearchCity: SearchCityByNameUseCase,
    private val useCaseGetLastFiveLocations: GetLastFiveLocationsUseCase,
    private val useCaseCheckPermission: CheckLocationPermissionUseCase,
) : ViewModel() {


    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    init{
        loadLocations()
        checkPermissions()
        checkSearch()
    }

    private fun loadLocations() {
        useCaseGetLastFiveLocations().onEach { result ->
            when (result) {

                is Result.Success<List<Location>> -> {
                    _state.update {
                        it.copy(
                            savedCities = result.data.toUi()
                        )
                    }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(

                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onCitySelected(location: LocationUi) {
        _state.update{it.copy(searchQuery = "")}
        viewModelScope.launch {
            useCaseSelectLocation(location.toDomain())
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    fun checkSearch() {
        _state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .debounce(300L)
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(Result.Success(emptyList<Location>()))
                } else {
                    useCaseSearchCity(query)
                }
            }
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _state.update { it.copy(searchedCities = result.data.toUi()) }
                    }

                    is Result.Error -> {
                    }
                }
            }
            .launchIn(viewModelScope)

    }

    fun checkPermissions() {
        val status = useCaseCheckPermission()
        if (status == PermissionStatus.Granted) {
            onPermissionGranted()
        } else {
            onPermissionResult(false, true)
        }
    }

    fun onPermissionResult(isGranted: Boolean, isRationaleNeeded: Boolean) {
        when {
            isGranted -> onPermissionGranted()
            isRationaleNeeded -> onShowRationale()
            else -> onDenied()
        }
    }

    fun onPermissionGranted() {
        _state.update { it.copy(permissionState = LocationPermissionState.Granted) }
        getUserLocation()
    }

    fun onShowRationale() {
        _state.update { it.copy(permissionState = LocationPermissionState.ShowRationale) }
    }

    fun onDenied() {
        _state.update { it.copy(permissionState = LocationPermissionState.Denied) }
    }

    fun getUserLocation() {
        useCaseGetCurentLocation().onEach { result ->
            when (result) {

                is Result.Success<Location> -> {
                    _state.update {
                        it.copy(
                            currentCity = result.data.toUi()
                        )
                    }
                }

                is Result.Error -> {
                }
            }
        }.launchIn(viewModelScope)
    }

}