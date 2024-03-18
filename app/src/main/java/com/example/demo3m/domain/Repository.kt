package com.example.demo3m.domain

import com.example.demo3m.data.network.response.RealtimeWeatherResponse
import com.example.demo3m.domain.model.RealtimeWeatherModel

interface Repository {
    suspend fun getRealtimeWeather(location: String, units: String): RealtimeWeatherModel?
}