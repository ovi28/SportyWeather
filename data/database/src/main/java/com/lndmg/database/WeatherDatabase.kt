package com.lndmg.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lndmg.database.entity.LocationEntity
import com.lndmg.database.entity.WeatherEntity

@Database(entities = [LocationEntity::class, WeatherEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}