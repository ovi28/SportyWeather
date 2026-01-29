package com.lndmg.repository

import com.lndmg.domain.interfaces.CityRepository
import com.lndmg.domain.interfaces.LocationPermissionRepository
import com.lndmg.domain.interfaces.LocationRepository
import com.lndmg.domain.interfaces.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    @Singleton
    abstract fun bindCityRepository(repository: CityRepositoryImpl): CityRepository


    @Binds
    @Singleton
    abstract fun bindWeatherRepository(repository: WeatherRepositoryImpl): WeatherRepository


    @Binds
    @Singleton
    abstract fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository

    @Binds
    @Singleton
    abstract fun bindPermissionRepository(
        impl: LocationPermissionRepositoryImpl
    ): LocationPermissionRepository
}

