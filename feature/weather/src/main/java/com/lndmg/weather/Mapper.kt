package com.lndmg.weather

import com.lndmg.domain.entity.Location
import com.lndmg.domain.entity.WeatherForecast
import com.lndmg.ui.LocationUi
import kotlin.Double

fun WeatherForecast.toUi() = WeatherUi(
    date = date,
    dateShort = date.split("-")[1] + "-" + date.split("-")[2],
    tempMin = tempMin,
    tempMax = tempMax,
    rain = rain
)

@JvmName("domainToUiWeather")
fun List<WeatherForecast>.toUi() = map(WeatherForecast::toUi)