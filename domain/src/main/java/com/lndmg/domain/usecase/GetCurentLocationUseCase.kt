package com.lndmg.domain.usecase

import com.lndmg.domain.entity.Location
import com.lndmg.domain.interfaces.LocationRepository
import com.lndmg.domain.util.Failure
import com.lndmg.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurentLocationUseCase @Inject constructor(
    private val repository: LocationRepository,
    private val searchCityByLatLongUseCase: SearchCityByLatLongUseCase
) {
    operator fun invoke(): Flow<Result<Location>> = flow {
        try {
            val locationResult = repository.getCurrentLocation()
            when(locationResult){
                is Result.Error -> locationResult
                is Result.Success -> {
                    val userLocation = locationResult.data
                    if(userLocation.name == ""){
                        val cityResult = searchCityByLatLongUseCase(userLocation.lat, userLocation.lng)
                        if(cityResult == null){
                            emit(Result.Error(Failure.LocationNotFound))
                        }else{
                            val detailedLocation = userLocation.copy(name = cityResult.name, longName = cityResult.longName)
                            repository.saveUserLocation(detailedLocation)
                            emit(Result.Success(detailedLocation))
                        }
                    }else{
                        emit(locationResult)
                    }
                }
            }
        } catch (e: Exception) {

            emit(Result.Error(Failure.LocationNotFound))
        }
    }
}