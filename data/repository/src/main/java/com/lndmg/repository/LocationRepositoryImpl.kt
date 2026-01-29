package com.lndmg.repository

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.lndmg.common.roundTo
import com.lndmg.database.WeatherDao
import com.lndmg.domain.entity.Location
import com.lndmg.domain.interfaces.LocationRepository
import com.lndmg.domain.util.Failure
import com.lndmg.domain.util.Result
import com.lndmg.repository.mapper.toDomain
import com.lndmg.repository.mapper.toLocal
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val localDataSource: WeatherDao,
    private val locationProvider: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<Location> {
        val dbLocation = localDataSource.getUserLocation().firstOrNull()
        val oneHourAgo = System.currentTimeMillis() - (1000 * 60 * 60)
        if(dbLocation != null){
            if(dbLocation.timestamp > oneHourAgo){
                return Result.Success(dbLocation.toDomain())
            }
        }
        val hasFineLocation = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFineLocation && !hasCoarseLocation) return Result.Error(Failure.LocationNotFound)
        return try {
           val locationUser =  locationProvider.lastLocation.await()
           val location = Location("", "", locationUser.latitude.roundTo(4), locationUser.longitude.roundTo(4), System.currentTimeMillis(), true)
           return Result.Success(location)
        } catch (e: Exception) {
            Result.Error(Failure.LocationNotFound)
        }
    }

    override suspend fun saveUserLocation(location: Location) {
        localDataSource.deleteUserLocation()
        localDataSource.insertLocation(location.toLocal())
    }


}