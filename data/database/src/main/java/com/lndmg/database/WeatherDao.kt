package com.lndmg.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lndmg.database.entity.LocationEntity
import com.lndmg.database.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Query("UPDATE location SET timestamp = :currentTime WHERE id = :locationId")
    suspend fun updateTimestamp(locationId: Int, currentTime: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM location WHERE isUserLocation = 0")
    suspend fun getLocationCount(): Int

    @Query("SELECT * FROM location WHERE isUserLocation = 1 ORDER BY timestamp DESC LIMIT 1")
    fun getUserLocation(): Flow<LocationEntity?>

    @Query("SELECT * FROM location ORDER BY timestamp DESC LIMIT 1")
    fun getLatestLocation(): LocationEntity?

    @Query("SELECT * FROM location WHERE isUserLocation = 0 ORDER BY timestamp DESC LIMIT 5")
    fun getLast5Locations(): Flow<List<LocationEntity>>

    @Query("DELETE FROM location WHERE isUserLocation = 0 AND id IN (SELECT id FROM location ORDER BY timestamp ASC LIMIT 1)")
    suspend fun deleteOldestLocation()

    @Query("DELETE FROM location WHERE isUserLocation = 1")
    suspend fun deleteUserLocation()

    @Transaction
    suspend fun insertWithLimit(location: LocationEntity) {
        if(location.isUserLocation){
            updateTimestamp(location.id)
        }else{
            insertLocation(location)
        }


        if (!location.isUserLocation && getLocationCount() > 5) {
            deleteOldestLocation()
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: List<WeatherEntity>)

    @Query("SELECT * FROM weather WHERE date = :date AND lat = :lat and lng=:lng LIMIT 1")
    fun getWeatherByDate(date: String, lat: Double, lng: Double): Flow<WeatherEntity?>

    @Query("SELECT * FROM weather WHERE date BETWEEN :startDate AND :endDate " +
            "AND (lat BETWEEN :lat - :tolerance AND :lat + :tolerance)" +
            "AND (lng BETWEEN :lng - :tolerance AND :lng + :tolerance)  LIMIT 7")
    fun getWeatherRange(endDate: String, startDate: String, lat: Double, lng: Double, tolerance: Double = 0.3): Flow<List<WeatherEntity>>

    @Query("DELETE FROM weather WHERE date < :todayDate")
    suspend fun deleteOutDated(todayDate: String)
}