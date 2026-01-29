package com.lndmg.domain.entity

data class WeatherForecast(
    val lat: Double,
    val lng: Double,
    val date: String,
    val tempMin: Double,
    val tempMax: Double,
    val rain: Int
)