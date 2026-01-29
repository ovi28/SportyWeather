package com.lndmg.domain.usecase

import com.lndmg.domain.entity.Location
import com.lndmg.domain.interfaces.CityRepository
import javax.inject.Inject

class SelectLocationUseCase  @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke(location : Location) {
       repository.selectLocation(location)
    }
}