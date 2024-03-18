package com.example.demo3m.ui.firstScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo3m.data.enums.LoadingState
import com.example.demo3m.domain.model.RealtimeWeatherModel
import com.example.demo3m.domain.usecase.GetRealTimeWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class FirstViewModel @Inject constructor(private val getRealTimeWeatherUseCase: GetRealTimeWeatherUseCase) :
    ViewModel() {
    private val _weatherData = MutableLiveData<RealtimeWeatherModel>()
    var weatherData: LiveData<RealtimeWeatherModel> = _weatherData

    private val _location = MutableLiveData<String>()
    val location: LiveData<String> = _location

    private val _units = MutableLiveData("imperial")
    val units: LiveData<String> = _units

    private val _loadingState = MutableLiveData(LoadingState.SUCCESS)
    var loadingState: LiveData<LoadingState> = _loadingState


    fun fetchWeather() {
        _loadingState.value = LoadingState.LOADING

        viewModelScope.launch {

            val response = withContext(Dispatchers.IO) {

                if (_location.value != null && _units.value != null) {
                    getRealTimeWeatherUseCase(_location.value!!, _units.value!!)
                } else {
                    null
                }
            }

            if (response != null){
                _weatherData.value = response!!
                _loadingState.value = LoadingState.SUCCESS
            } else {
                _loadingState.value = LoadingState.ERROR
            }

        }
    }

    fun onChangeLocationValue(location: String) {
        _location.value = location
    }


    fun changeUnits() {
        _units.value = if (_units.value == "imperial") "metric" else "imperial"
        changeValueUnits()
    }

    fun changeValueUnits() {
        if (_weatherData.value == null) return
        _weatherData.value = _weatherData.value?.let {
            if (_units.value == "metric") {
                it.copy(temperature = round2Decimals(((it.temperature - 32) * 5 / 9), 2))
            } else {
                it.copy(temperature = round2Decimals((it.temperature * 9 / 5 + 32), 2))
            }
        }
    }

    fun round2Decimals(number: Double, numDecimalPlaces: Int): Double {
        return number.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }



}