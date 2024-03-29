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
        @Query("units") units: String? = "metric",
        @Query("lang") lang: String? = "en",
        @Query("appid") appid: String? = "c687f9659a452c95a84f05c506629873"
    ): WeatherResponse


    @GET("onecall")
    fun getAlert(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") appid: String? = "c687f9659a452c95a84f05c506629873"
    ): Alert
}

/*
 @GET("forecast?")
    fun getCurrentWeather(
        @Query("lat")
        lat : String,
        @Query("lon")
        lon : String,
        @Query("appid")
        appid : String = Utils.API_KEY,

    ): Call<ForeCast>

    @GET("forecast?")
    fun getWeatherByCity(
        @Query("q")
        city : String,
        @Query("appid")
        appid : String = Utils.API_KEY,

        ): Call<ForeCast>
 */