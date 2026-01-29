package com.lndmg.domain.entity

data class Location (
    val name: String,
    val longName: String,
    val lat: Double,
    val lng: Double,
    val timeStamp: Long,
    val isUserLocation: Boolean
)