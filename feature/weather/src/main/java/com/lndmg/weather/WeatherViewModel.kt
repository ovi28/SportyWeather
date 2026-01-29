package com.lndmg.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lndmg.domain.entity.Location
import com.lndmg.domain.entity.WeatherForecast
import com.lndmg.domain.usecase.GetSelectedLocationUseCase
import com.lndmg.domain.usecase.GetWeatherWeeklyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import com.lndmg.weather.WeatherState
import com.lndmg.domain.util.Result
import com.lndmg.ui.toUi
import com.lndmg.weather.toUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurentLocationUseCase: GetSelectedLocationUseCase,
    private val getWeatherWeeklyUseCase: GetWeatherWeeklyUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(WeatherState())
    val state = _state.onStart {
        getCity()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), WeatherState())

    fun getCity(){
        viewModelScope.launch {
            val result = getCurentLocationUseCase()
            if(result is Result.Success){
                _state.update { it.copy(location = result.data.toUi()) }
                getWeather(result.data.lat, result.data.lng)
            }else{
                Log.d("TAG", "getCity: Error")
            }
        }
    }

    fun getWeather(lat: Double, lng: Double){
        getWeatherWeeklyUseCase(lat, lng).onEach { result ->
            when (result) {

                is Result.Success<List<WeatherForecast>> -> {
                    _state.update {
                        it.copy(
                           weatherList = result.data.toUi()
                        )
                    }
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(

                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }


}