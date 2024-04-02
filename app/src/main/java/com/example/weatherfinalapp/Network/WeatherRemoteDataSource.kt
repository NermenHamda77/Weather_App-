package com.example.weatherfinalapp.Network

import com.example.weatherfinalapp.model.WeatherResponse

interface WeatherRemoteDataSource {
    suspend fun makeNetworkCall(
        lat: Double?,
        lon: Double?,
        units: String? = "metric",
        lang: String? = "en",
        appid: String? = "c687f9659a452c95a84f05c506629873"
    ): WeatherResponse

}