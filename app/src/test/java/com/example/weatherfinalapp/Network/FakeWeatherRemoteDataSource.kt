package com.example.weatherfinalapp.Network

import com.example.weatherfinalapp.model.WeatherResponse

class FakeWeatherRemoteDataSource(private val weatherResponse: WeatherResponse):WeatherRemoteDataSource {
    override suspend fun makeNetworkCall(
        lat: Double?,
        lon: Double?,
        units: String?,
        lang: String?,
        appid: String?
    ): WeatherResponse {
       return weatherResponse
    }
}