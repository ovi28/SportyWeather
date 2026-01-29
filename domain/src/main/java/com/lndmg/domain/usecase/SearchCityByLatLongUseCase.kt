package com.lndmg.domain.usecase

import com.lndmg.domain.entity.Location
import com.lndmg.domain.interfaces.CityRepository
import javax.inject.Inject

class SearchCityByLatLongUseCase  @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke(lat: Double, lng: Double) : Location?{
        return repository.searchCityByLatLong(lat, lng)
    }
}