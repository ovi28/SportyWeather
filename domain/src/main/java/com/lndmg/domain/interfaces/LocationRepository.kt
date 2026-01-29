package com.lndmg.domain.interfaces

import com.lndmg.domain.entity.Location
import com.lndmg.domain.util.Result

interface LocationRepository {
    suspend fun getCurrentLocation() : Result<Location>
    suspend fun saveUserLocation(location: Location)

}