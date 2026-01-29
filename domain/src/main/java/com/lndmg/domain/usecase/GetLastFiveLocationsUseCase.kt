package com.lndmg.domain.usecase

import com.lndmg.domain.entity.Location
import com.lndmg.domain.interfaces.CityRepository
import com.lndmg.domain.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastFiveLocationsUseCase @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<Result<List<Location>>> {
        return repository.getLastFive()
    }
}