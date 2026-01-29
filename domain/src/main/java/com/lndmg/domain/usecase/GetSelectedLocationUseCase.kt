package com.lndmg.domain.usecase

import com.lndmg.domain.entity.Location
import com.lndmg.domain.util.Result
import com.lndmg.domain.interfaces.CityRepository
import javax.inject.Inject

class GetSelectedLocationUseCase  @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke() : Result<Location> {
       return repository.getLastLocation()
    }
}