package com.lndmg.domain.interfaces

import com.lndmg.domain.entity.PermissionStatus

interface LocationPermissionRepository {
    fun checkLocationPermission(): PermissionStatus
}