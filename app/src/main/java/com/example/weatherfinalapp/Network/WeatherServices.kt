package com.example.weatherfinalapp.Network

import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServices {

    @GET("forecast")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("units") units: String? ,
        @Query("lang") lang: String?,
        @Query("appid") appid: String? = "c687f9659a452c95a84f05c506629873"
    ): WeatherResponse


}

