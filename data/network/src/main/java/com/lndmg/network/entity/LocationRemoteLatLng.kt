package com.lndmg.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationRemoteLatLng(
    @SerialName("display_name")val longName: String,
    @SerialName("lat")val latitude: Double,
    @SerialName("lon")val longitude: Double,
    @SerialName("address")val address: Address
)


@Serializable
data class Address(
    @SerialName("city") val city: String,
    @SerialName("country") val country: String
)