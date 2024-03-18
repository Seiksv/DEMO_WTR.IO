package com.example.demo3m.domain.usecase

import com.example.demo3m.domain.Repository
import javax.inject.Inject

class GetRealTimeWeatherUseCase  @Inject constructor(private val repository: Repository) {

        suspend operator fun invoke(location: String, units: String) = repository.getRealtimeWeather(location, units)

}