package com.lndmg.domain.interfaces

import com.lndmg.domain.entity.Location
import com.lndmg.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    fun searchCityByName(name: String) : Flow<Result<List<Location>>>
    fun getLastFive() : Flow<Result<List<Location>>>
    suspend fun selectLocation(location: Location)
    suspend fun getLastLocation() : Result<Location>
    suspend fun searchCityByLatLong(lat: Double, lng: Double) : Location?
}