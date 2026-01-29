package com.lndmg.sportyweather.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lndmg.search.SearchScreen
import com.lndmg.weather.WeatherDetailScreen

@Composable
fun MainNavigation(
    modifier: Modifier  = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = "list", modifier = modifier) {
        composable("list") {
            SearchScreen(
                onCitySelected = {
                    navController.navigate("details")
                }
            )
        }
        composable(
            route = "details",
        ) { backStackEntry ->
            WeatherDetailScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}