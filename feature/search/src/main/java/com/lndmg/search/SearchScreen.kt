package com.lndmg.search

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lndmg.ui.LocationUi

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onCitySelected: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color(0xFF4A90E2),
        topBar = {
            SearchBar(
                query = state.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(padding)
        ) {
            if(!state.searchedCities.isEmpty()){
                item {
                    Text(
                        text = stringResource(R.string.searched_locations),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                    )
                }
            }

            items(state.searchedCities) { city ->
                CityCard(city, onClick = {
                    viewModel.onCitySelected(city)
                    onCitySelected() })
            }

            item {
                Text(
                    text = stringResource(R.string.current_location),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item() {
                LocationPermissionScreen(
                    onCitySelected = { location -> viewModel.onCitySelected(location)
                        onCitySelected()},
                    currentCity = state.currentCity,
                    state = state.permissionState,
                    permissionGranted = { viewModel.onPermissionGranted() },
                    onPermissionResult = { isGranted, showRationale ->
                        viewModel.onPermissionResult(
                            isGranted,
                            showRationale
                        )
                    })

            }

            item {
                Text(
                    text = stringResource(R.string.history),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
            }

            items(state.savedCities) { city ->
                CityCard(city, onClick = {
                    viewModel.onCitySelected(city)
                    onCitySelected() })
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search for a city...", color = Color.White.copy(alpha = 0.7f)) },
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White.copy(alpha = 0.2f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.2f),
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun CityCard(location: LocationUi, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}

@Composable
fun LocationPermissionScreen(
    state: LocationPermissionState,
    permissionGranted: () -> Unit,
    onPermissionResult: (Boolean, Boolean) -> Unit,
    onCitySelected: (LocationUi) -> Unit,
    currentCity: LocationUi?,
) {

    val context = LocalContext.current

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocation = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        val isGranted = fineLocation || coarseLocation
        val activity = context as? Activity
        val showRationale =
            activity?.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) == true

        onPermissionResult(isGranted, showRationale)
    }

    val launchSystemPermissionDialog = {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val checkAndRequest = {
        val activity = context as? Activity
        val showRationale =
            activity?.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) == true

        if (showRationale) {
            onPermissionResult(false, true)
        } else {
            launchSystemPermissionDialog()
        }
    }

    LocationScreenContent(
        onCitySelected = onCitySelected,
        currentCity = currentCity,
        state = state,
        onRequestPermission = checkAndRequest,
        onRationaleConfirm = {
            permissionGranted()
            launchSystemPermissionDialog()
        }
    )

}

@Composable
fun LocationScreenContent(
    onCitySelected: (LocationUi) -> Unit,
    currentCity: LocationUi?,
    state: LocationPermissionState,
    onRequestPermission: () -> Unit,
    onRationaleConfirm: () -> Unit,
) {
    when (state) {
        is LocationPermissionState.Idle -> {
            Button(onClick = onRequestPermission) {
                Text("Get Location")
            }
        }

        is LocationPermissionState.Granted -> {
            currentCity?.let {
                CityCard(currentCity, onClick = { onCitySelected(currentCity) })
            }

        }

        is LocationPermissionState.ShowRationale -> {
            onRationaleConfirm()
        }

        is LocationPermissionState.Denied -> {
            Column {
                Text("Permission was permanently denied. Please enable it in settings.")
            }
        }
    }
}

