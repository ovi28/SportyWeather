package com.lndmg.weather

data class WeatherUi(
    val date: String,
    val dateShort: String,
    val tempMin: Double,
    val tempMax: Double,
    val rain: Int
)