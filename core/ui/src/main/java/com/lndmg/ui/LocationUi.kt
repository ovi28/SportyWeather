package com.lndmg.ui

data class LocationUi (
    val name: String,
    val longName: String,
    val lat: Double,
    val lng: Double,
    val isUserLocation: Boolean,
    val timeStamp: Long
)