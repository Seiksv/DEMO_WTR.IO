package com.example.demo3m.data.network

import com.example.demo3m.data.network.response.RealtimeWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather/realtime")
    suspend fun getRealtimeWeather(
        @Query("location") location: String,
        @Query("apikey") apiKey: String,
        @Query("units") units: String // Add units parameter
    ): Response<RealtimeWeatherResponse>
}