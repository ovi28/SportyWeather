package com.lndmg.network.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationRemote(
    @SerialName("display_place")val name: String,
    @SerialName("display_name")val longName: String,
    @SerialName("lat")val latitude: Double,
    @SerialName("lon")val longitude: Double,
    @SerialName("address")val address: AddressRemote
)
@Serializable
data class AddressRemote(
    @SerialName("country") val country: String
)