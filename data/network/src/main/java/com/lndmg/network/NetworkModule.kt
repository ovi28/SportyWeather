package com.lndmg.network

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import com.lndmg.network.BuildConfig
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocationApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherApi

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL_LOCATION = "https://api.locationiq.com/"
    private const val BASE_URL_WEATHER = "https://api.open-meteo.com/"
    private const val TIMEOUT = 30L

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    @LocationApi
    fun provideLocationInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            val url = originalUrl.newBuilder()
                .addQueryParameter("key", BuildConfig.MAP_API_KEY)
                .build()

            val request = originalRequest.newBuilder()
                .url(url)
                .build()

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    @LocationApi
    fun provideOkHttpClientLocation(@LocationApi locationInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(locationInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @LocationApi
    fun provideRetrofitLocation(@LocationApi okHttpClient: OkHttpClient): Retrofit {
        val networkJson = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(BASE_URL_LOCATION)
            .client(okHttpClient)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    @Provides
    @Singleton
    fun provideLocationApi(@LocationApi retrofit: Retrofit): LocationApiService {
        return retrofit.create(LocationApiService::class.java)
    }



    @Provides
    @Singleton
    @WeatherApi
    fun provideWeatherInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url
            val request = originalRequest.newBuilder()
                .url(originalUrl)
                .build()

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    @WeatherApi
    fun provideOkHttpClientWeather(@WeatherApi weatherInterceptor: Interceptor, loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(weatherInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @WeatherApi
    fun provideRetrofitWeather(@WeatherApi okHttpClient: OkHttpClient): Retrofit {
        val networkJson = Json { ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true}
        return Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .client(okHttpClient)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(@WeatherApi retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

}


