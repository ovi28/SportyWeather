package com.lndmg.repository.mapper

import com.lndmg.common.roundTo
import com.lndmg.database.entity.LocationEntity
import com.lndmg.domain.entity.Location
import com.lndmg.network.entity.LocationRemote
import com.lndmg.network.entity.LocationRemoteLatLng

fun LocationEntity.toDomain() = Location(
    name = name,
    longName = longName,
    lat = lat,
    lng = lng,
    timeStamp = timestamp,
    isUserLocation = isUserLocation
)

fun Location.toLocal() = LocationEntity(
    name = name,
    longName = longName,
    lat = lat.roundTo(4),
    lng = lng.roundTo(4),
    isUserLocation = isUserLocation,
    timestamp = timeStamp
)

fun LocationRemote.toDomain() = Location(
    name = name +", " + address.country,
    longName = longName,
    lat = latitude.roundTo(4),
    lng = longitude.roundTo(4),
    timeStamp = System.currentTimeMillis(),
    isUserLocation = false
)


fun LocationRemoteLatLng.toDomain() = Location(
    name = address.city + ", " + address.country,
    longName = longName,
    lat = latitude.roundTo(4),
    lng = longitude.roundTo(4),
    timeStamp = System.currentTimeMillis(),
    isUserLocation = true
)

@JvmName("localToDomain")
fun List<LocationEntity>.toDomain() = map(LocationEntity::toDomain)