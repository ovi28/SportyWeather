package com.lndmg.repository

import com.lndmg.common.IoDispatcher
import com.lndmg.database.WeatherDao
import com.lndmg.domain.entity.Location
import com.lndmg.domain.interfaces.CityRepository
import com.lndmg.domain.util.Failure
import kotlinx.coroutines.flow.Flow
import com.lndmg.domain.util.Result
import com.lndmg.network.LocationApiService
import com.lndmg.repository.mapper.toDomain
import com.lndmg.repository.mapper.toLocal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val localDataSource: WeatherDao,
    private val remoteDataSource: LocationApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : CityRepository {

    override fun searchCityByName(name: String): Flow<Result<List<Location>>> = flow{
        try {
            val remoteData = remoteDataSource.getCityByName(query = name)
            val domainData = remoteData.map { it.toDomain() }
            emit(Result.Success(domainData))
        } catch (e: Exception) {
            emit(Result.Error(Failure.ServerError))
        }
    }.flowOn(dispatcher)

    override fun getLastFive(): Flow<Result<List<Location>>> {
        return localDataSource.getLast5Locations().map {
            locations ->
            val location = locations.map{it.toDomain()}
            Result.Success(location)
        }.catch{
            Result.Error(Failure.NoData)
        }
    }

    override suspend fun selectLocation(location: Location) {
        localDataSource.insertWithLimit(location.toLocal())
    }

    override suspend fun getLastLocation(): Result<Location> = withContext(dispatcher){
        if( localDataSource.getLatestLocation() == null){
            Result.Error(Failure.NoData)
        }else{
            Result.Success(localDataSource.getLatestLocation()!!.toDomain())
        }
    }

    override suspend fun searchCityByLatLong(
        lat: Double,
        lng: Double,
    ): Location {
        val remote = remoteDataSource.getCityByLatLng(lat, lng)
        return remote.toDomain()
    }
}