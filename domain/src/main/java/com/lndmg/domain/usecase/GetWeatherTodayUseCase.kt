package com.lndmg.domain.usecase

import com.lndmg.domain.entity.WeatherForecast
import com.lndmg.domain.util.Result
import com.lndmg.domain.interfaces.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherTodayUseCase@Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(lat: Double, lng: Double): Flow<Result<WeatherForecast>> {
        return repository.getWeatherToday(lat, lng)
    }
}