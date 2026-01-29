package com.lndmg.network

import com.lndmg.network.entity.LocationRemote
import com.lndmg.network.entity.LocationRemoteLatLng
import retrofit2.http.GET
import retrofit2.http.Query

interface  LocationApiService {

    @GET("v1/autocomplete")
    suspend fun getCityByName(
        @Query("q") query: String,
        @Query("limit") limit: Int = 5,
        @Query("dedupe") dedupe: Int = 1
    ): List<LocationRemote>


    @GET("v1/reverse")
    suspend fun getCityByLatLng(
        @Query("lat") lat: Double,
        @Query("lon") lng : Double,
        @Query("format") format: String = "json",
        @Query("normalizeaddress") normalizeAddress: Int = 1
    ): LocationRemoteLatLng

}