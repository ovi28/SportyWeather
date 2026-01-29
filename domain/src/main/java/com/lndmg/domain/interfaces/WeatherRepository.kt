package com.lndmg.domain.interfaces

import com.lndmg.domain.entity.WeatherForecast
import com.lndmg.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getWeatherToday(lat: Double, lng: Double) : Flow<Result<WeatherForecast>>
    fun getWeatherWeekly(lat: Double, lng: Double) : Flow<Result<List<WeatherForecast>>>
}