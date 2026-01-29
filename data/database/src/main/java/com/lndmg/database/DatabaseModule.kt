package com.lndmg.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): WeatherDatabase{
        return Room.databaseBuilder(
            appContext.applicationContext,
            WeatherDatabase::class.java,
            "weather.db"
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun provideTransactionDao(database: WeatherDatabase): WeatherDao = database.weatherDao()
}