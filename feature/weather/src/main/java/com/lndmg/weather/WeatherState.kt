package com.lndmg.weather

import androidx.compose.runtime.Immutable
import com.lndmg.ui.LocationUi

@Immutable
data class WeatherState(
    val weatherList: List<WeatherUi> = emptyList(),
    val location: LocationUi? = null
)