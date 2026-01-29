package com.lndmg.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherDayRemote(
     @SerialName("time") val time: List<String>,
     @SerialName("temperature_2m_max") val temperatureMax: List<Double>,
     @SerialName("temperature_2m_min")  val temperatureMin: List<Double>,
     @SerialName("precipitation_probability_max") val precipitationProbabilityMax: List<Int>
)