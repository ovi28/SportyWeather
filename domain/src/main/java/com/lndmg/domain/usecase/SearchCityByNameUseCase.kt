package com.lndmg.domain.usecase

import com.lndmg.domain.entity.Location
import com.lndmg.domain.interfaces.CityRepository
import kotlinx.coroutines.flow.Flow
import com.lndmg.domain.util.Result
import javax.inject.Inject

class SearchCityByNameUseCase  @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(name: String): Flow<Result<List<Location>>> {
        return repository.searchCityByName(name)
    }
}
