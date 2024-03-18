package com.example.demo3m.data.network.module

import com.example.demo3m.data.RepositoryImpl
import com.example.demo3m.data.network.WeatherApiService
import com.example.demo3m.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.tomorrow.io/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }

    @Provides
    fun provideRepository(apiService: WeatherApiService): Repository {
        return RepositoryImpl(apiService)
    }
}