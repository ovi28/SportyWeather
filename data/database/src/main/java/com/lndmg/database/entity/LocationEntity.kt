package com.lndmg.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "location",
    indices = [Index(value = ["name", "lat", "lng"], unique = true)])
data class LocationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val longName: String,
    val lat: Double,
    val lng: Double,
    val isUserLocation: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)