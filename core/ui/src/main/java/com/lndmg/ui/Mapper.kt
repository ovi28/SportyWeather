package com.lndmg.ui

import com.lndmg.domain.entity.Location

fun Location.toUi() = LocationUi(
    name = name,
    longName = longName,
    lat = lat,
    lng = lng,
    timeStamp = timeStamp,
    isUserLocation = isUserLocation
)

fun LocationUi.toDomain() = Location(
    name = name,
    longName = longName,
    lat = lat,
    lng = lng,
    timeStamp = timeStamp,
    isUserLocation = isUserLocation
)

@JvmName("domainToUiLocation")
fun List<Location>.toUi() = map(Location::toUi)