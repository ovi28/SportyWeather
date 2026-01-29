package com.lndmg.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.lndmg.domain.entity.PermissionStatus
import com.lndmg.domain.interfaces.LocationPermissionRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationPermissionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationPermissionRepository {

    override fun checkLocationPermission(): PermissionStatus {
        val hasFine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return if (hasFine || hasCoarse) {
            PermissionStatus.Granted
        } else {
            PermissionStatus.Denied
        }
    }
}