package com.example.demo3m.data

import android.util.Log
import com.example.demo3m.data.network.WeatherApiService
import com.example.demo3m.data.network.response.RealtimeWeatherResponse
import com.example.demo3m.domain.Repository
import com.example.demo3m.domain.model.RealtimeWeatherModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val apiService: WeatherApiService): Repository {

    override suspend fun getRealtimeWeather(location: String, units: String): RealtimeWeatherModel? {
       runCatching { apiService.getRealtimeWeather(location, "8MOzdzNdR7HzRRdQ9kCAi92gm2ggPMol", units) }
           .onSuccess {
               Log.i("RepositoryImpl", "Success: ${it.body()}")
               return it.body()?.toDomain() }
           .onFailure { Log.i("RepositoryImpl", "Error: ${it.message}") }

        return null
    }

    //prueba
    //8MOzdzNdR7HzRRdQ9kCAi92gm2ggPMol

    //TEST
    //OTh8ITGsXJx3490u3IshfSz0j45BZo0x

}
