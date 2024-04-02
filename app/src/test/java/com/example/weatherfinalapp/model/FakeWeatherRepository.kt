package com.example.weatherfinalapp.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRepository : WeatherRepository {
    private val favoriteLocations : MutableList<FavoriteLocation>? = mutableListOf()
    private val alerts : MutableList<Alert>? = mutableListOf()
    override fun getStoredFavLocations(): Flow<List<FavoriteLocation>> {
        return flow {
            val favLocation = favoriteLocations?.toList()
            if(favLocation.isNullOrEmpty()){
                emit(emptyList())
            }else{
                emit(favLocation)
            }

        }
    }

    override suspend fun insertLocation(location: FavoriteLocation) {
        favoriteLocations?.add(location)
    }

    override suspend fun deleteLocation(location: FavoriteLocation) {
        favoriteLocations?.remove(location)
    }

    override fun getAllAlerts(): Flow<List<Alert>> {
        return flow {
            val alert = alerts?.toList()
            if(alert.isNullOrEmpty()){
                emit(emptyList())
            }else{
                emit(alert)
            }

        }
    }

    override suspend fun insertAlert(alert: Alert) {
        alerts?.add(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        alerts?.remove(alert)
    }

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        units: String?,
        lang: String?,
        appid: String?
    ): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }
}