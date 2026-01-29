package com.lndmg.domain.usecase

import com.lndmg.domain.entity.WeatherForecast
import com.lndmg.domain.interfaces.WeatherRepository
import kotlinx.coroutines.flow.Flow
import com.lndmg.domain.util.Result
import javax.inject.Inject

class GetWeatherWeeklyUseCase@Inject constructor(
    private val repository: WeatherRepository
) {
    operator fun invoke(lat: Double, lng: Double): Flow<Result<List<WeatherForecast>>> {
        return repository.getWeatherWeekly(lat, lng)
    }
}