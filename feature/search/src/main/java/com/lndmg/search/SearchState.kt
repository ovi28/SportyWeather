package com.lndmg.search

import androidx.compose.runtime.Immutable
import com.lndmg.ui.LocationUi

@Immutable
data class SearchState(
    val searchQuery: String = "",
    val savedCities: List<LocationUi> = emptyList(),
    val searchedCities: List<LocationUi> = emptyList(),
    val currentCity: LocationUi? = null,
    val permissionState: LocationPermissionState = LocationPermissionState.Idle
)