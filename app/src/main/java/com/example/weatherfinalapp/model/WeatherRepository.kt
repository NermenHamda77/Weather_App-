package com.example.weatherfinalapp.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface WeatherRepository {

    fun getStoredFavLocations() : Flow<List<FavoriteLocation>>

    suspend fun insertLocation(location: FavoriteLocation)

    suspend fun deleteLocation(location: FavoriteLocation)


    ///Alert

    fun getAllAlerts(): Flow<List<Alert>>


    suspend fun insertAlert(alert: Alert)


    suspend fun deleteAlert(alert: Alert)


    suspend fun getWeather(
        lat : Double ,
        lon : Double ,
        units : String?  ,   //= "metric"
        lang : String?  ,   //= "en"
        appid : String? = "c687f9659a452c95a84f05c506629873"
    ): Flow<WeatherResponse>



}