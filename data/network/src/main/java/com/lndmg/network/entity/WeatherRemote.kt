package com.lndmg.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherRemote(
    @SerialName("latitude") val lat: Double,
    @SerialName("longitude")val lng: Double,
    @SerialName("daily")val dailyWeather : WeatherDayRemote
)