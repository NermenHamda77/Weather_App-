package com.example.weatherfinalapp.db

import android.content.Context
import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.FavoriteLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class WeatherLocalDataSourceImp(context: Context) : WeatherLocalDataSource {

    private val weatherDAO : WeatherDAO by lazy {
        val db: AppDataBase = AppDataBase.getInstance(context)
        db.getWeatherDAO()
    }



    companion object {
        private var localSource: WeatherLocalDataSourceImp? = null

        fun getInstance(context: Context): WeatherLocalDataSourceImp {
            if (localSource == null) {
                localSource = WeatherLocalDataSourceImp(context)
            }
            return localSource!!
        }
    }



    override  fun getAllStoredFavLocations(): Flow<List<FavoriteLocation>>{
        return weatherDAO.getAllLocations()
    }

    override suspend fun addLocation(location: FavoriteLocation) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherDAO.insertLocation(location)
        }
    }

    override suspend fun removeLocation(location: FavoriteLocation) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherDAO.deleteLocation(location)
        }
    }


    /// Alert
    override fun getAllStoredAlerts(): Flow<List<Alert>>{
        return weatherDAO.getAllAlerts()
    }

    override suspend fun addAlert(alert: Alert) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherDAO.insertAlert(alert)
        }
    }

    override suspend fun removeAlert(alert: Alert) {
        CoroutineScope(Dispatchers.IO).launch {
            weatherDAO.deleteAlert(alert)
        }
    }


}


