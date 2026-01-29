package com.lndmg.network

import com.lndmg.network.entity.LocationRemote
import com.lndmg.network.entity.WeatherRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("v1/forecast")
    suspend fun getWeatherForLocation(
        @Query("latitude") lat: Double,
        @Query("longitude") lng: Double,
        @Query("daily") daily: String = "temperature_2m_max,temperature_2m_min,precipitation_probability_max"
    ): WeatherRemote

}