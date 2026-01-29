package com.lndmg.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lat: Double,
    val lng: Double,
    val date: String,
    val tempMin: Double,
    val tempMax: Double,
    val rain: Int
)