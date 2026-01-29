package com.lndmg.repository

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.lndmg.common.IoDispatcher
import com.lndmg.database.WeatherDao
import com.lndmg.domain.entity.WeatherForecast
import com.lndmg.domain.interfaces.WeatherRepository
import com.lndmg.domain.util.Failure
import com.lndmg.network.WeatherApiService
import com.lndmg.repository.mapper.toLocal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import com.lndmg.domain.util.Result
import com.lndmg.repository.mapper.toDomain
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
class WeatherRepositoryImpl @Inject constructor(
    private val networkDataSource: WeatherApiService,
    private val localDataSource: WeatherDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : WeatherRepository {

    override fun getWeatherToday(
        lat: Double,
        lng: Double,
    ): Flow<Result<WeatherForecast>> = flow{

        val currentLocal = localDataSource.getWeatherByDate(LocalDate.now().toString(), lat, lng).firstOrNull()
        if (currentLocal == null) {
            val networkResult = getRemoteWeather(lat, lng)
            if (networkResult is Result.Error) {
                emit(networkResult)
            }
        }
        val dbStream = localDataSource.getWeatherByDate(LocalDate.now().toString(), lat, lng)
            .filterNotNull()
            .map { Result.Success(it.toDomain()) }

        emitAll(dbStream)
    }.flowOn(dispatcher)

    override fun getWeatherWeekly(
        lat: Double,
        lng: Double,
    ): Flow<Result<List<WeatherForecast>>> = flow {
        val today = LocalDate.now()
        val endOfWeek = today.plusDays(6)

        val weeklyStream = localDataSource.getWeatherRange(
            startDate = today.toString(),
            endDate = endOfWeek.toString(),
            lat = lat,
            lng = lng
        ).map { list ->
            if (list.isEmpty()) {
                getRemoteWeather(lat, lng)
                Result.Error(Failure.NoData)
            }
            else Result.Success(list.map { it.toDomain() })
        }
        emitAll(weeklyStream)
    }

    suspend fun getRemoteWeather(
        lat: Double,
        lng: Double,
    ) {
        val remoteTasks = networkDataSource.getWeatherForLocation(lat = lat, lng = lng)
        localDataSource.deleteOutDated(LocalDate.now().toString())
        localDataSource.insertWeather(remoteTasks.toLocal())
    }
}