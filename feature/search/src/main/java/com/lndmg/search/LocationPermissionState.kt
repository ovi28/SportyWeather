package com.lndmg.search

sealed class LocationPermissionState {
    object Idle : LocationPermissionState()
    object Granted : LocationPermissionState()
    object Denied : LocationPermissionState()
    object ShowRationale : LocationPermissionState()
}