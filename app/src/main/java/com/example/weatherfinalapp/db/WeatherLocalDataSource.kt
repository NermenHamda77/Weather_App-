package com.example.weatherfinalapp.db

import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.FavoriteLocation
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun getAllStoredFavLocations(): Flow<List<FavoriteLocation>>
    suspend fun addLocation(location: FavoriteLocation)

    suspend fun removeLocation(location: FavoriteLocation)

    /// Alert
    fun getAllStoredAlerts(): Flow<List<Alert>>
    suspend fun addAlert(alert: Alert)

    suspend fun removeAlert(alert: Alert)

}