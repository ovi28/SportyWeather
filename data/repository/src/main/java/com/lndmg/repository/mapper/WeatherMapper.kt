package com.lndmg.repository.mapper

import com.lndmg.common.roundTo
import com.lndmg.database.entity.WeatherEntity
import com.lndmg.domain.entity.WeatherForecast
import com.lndmg.network.entity.WeatherRemote

fun WeatherRemote.toLocal() : List<WeatherEntity>{
    val daily = this.dailyWeather

    return daily.time.indices.map { index ->
        WeatherEntity(
            lat = this.lat.roundTo(4),
            lng = this.lng.roundTo(4),
            date = daily.time[index],
            tempMax = daily.temperatureMax.getOrElse(index) { 0.0 }.roundTo(2),
            tempMin = daily.temperatureMin.getOrElse(index) { 0.0 }.roundTo(2),
            rain = daily.precipitationProbabilityMax.getOrElse(index) { 0 }
        )
    }
}

fun WeatherEntity.toDomain()  = WeatherForecast(
    lat = lat,
    lng = lng,
    date = date,
    tempMin = tempMin,
    tempMax = tempMax,
    rain = rain
)