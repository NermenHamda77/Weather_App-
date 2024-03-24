package com.example.weatherfinalapp.model

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getStoredFavLocations() : Flow<List<FavoriteLocation>>

    suspend fun insertLocation(location: FavoriteLocation)

    suspend fun deleteLocation(location: FavoriteLocation)

    suspend fun getWeather(
        lat : Double ,
        lon : Double ,
        units : String? = "metric" ,
        lang : String? = "en" ,
        appid : String? = "c687f9659a452c95a84f05c506629873"
    ): Flow<WeatherResponse>

     suspend fun getWeatherOfCity(
        city: String,
        appid: String? = "c687f9659a452c95a84f05c506629873"
    ): Flow<WeatherResponse>

}