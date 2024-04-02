package com.example.weatherfinalapp.db

import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.FavoriteLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(
    private val favoriteLocations : MutableList<FavoriteLocation>? = mutableListOf(),
    private val alerts : MutableList<Alert>? = mutableListOf()

) : WeatherLocalDataSource {

    override fun getAllStoredFavLocations(): Flow<List<FavoriteLocation>> {
        return flow {
            val favLocation = favoriteLocations?.toList()
            if(favLocation.isNullOrEmpty()){
                emit(emptyList())
            }else{
                emit(favLocation)
            }

        }
    }

    override suspend fun addLocation(location: FavoriteLocation) {
        favoriteLocations?.add(location)
    }

    override suspend fun removeLocation(location: FavoriteLocation) {
        favoriteLocations?.remove(location)
    }

    override fun getAllStoredAlerts(): Flow<List<Alert>> {
        return flow {
            val alert = alerts?.toList()
            if(alert.isNullOrEmpty()){
                emit(emptyList())
            }else{
                emit(alert)
            }

        }
    }

    override suspend fun addAlert(alert: Alert) {
        alerts?.add(alert)
    }

    override suspend fun removeAlert(alert: Alert) {
        alerts?.remove(alert)
    }
}