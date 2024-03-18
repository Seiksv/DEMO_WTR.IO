package com.example.demo3m.data.network.response

import com.example.demo3m.domain.model.RealtimeWeatherModel
import com.google.gson.annotations.SerializedName

data class RealtimeWeatherResponse(
    @SerializedName("data") val data: RealtimeWeatherData,
    @SerializedName("location") val location: Location
) {
    fun toDomain(): RealtimeWeatherModel {
        return RealtimeWeatherModel(
            temperature = data.values.temperature,
            name = location.name
        )
    }
}

data class RealtimeWeatherData(
    @SerializedName("time") val time: String,
    @SerializedName("values") val values: WeatherValues
)

data class WeatherValues(

    @SerializedName("temperature") val temperature: Double,
    @SerializedName("temperatureApparent") val temperatureApparent: Double,
    @SerializedName("uvHealthConcern") val uvHealthConcern: Int,
    @SerializedName("uvIndex") val uvIndex: Int,
    @SerializedName("visibility") val visibility: Double,
    @SerializedName("weatherCode") val weatherCode: Int,
    @SerializedName("windDirection") val windDirection: Double,
    @SerializedName("windGust") val windGust: Double,
    @SerializedName("windSpeed") val windSpeed: Double
)

data class Location(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("name") val name: String,
)