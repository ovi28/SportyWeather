package com.lndmg.domain.usecase

import com.lndmg.domain.entity.PermissionStatus
import com.lndmg.domain.interfaces.LocationPermissionRepository
import javax.inject.Inject

class CheckLocationPermissionUseCase @Inject constructor(
    private val repository: LocationPermissionRepository
) {
    operator fun invoke(): PermissionStatus {
        return repository.checkLocationPermission()
    }
}