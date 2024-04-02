package com.example.weatherfinalapp.model

import com.example.weatherfinalapp.Network.WeatherRemoteDataSource
import com.example.weatherfinalapp.Network.WeatherRemoteDataSourceImp
import com.example.weatherfinalapp.db.WeatherLocalDataSource
import com.example.weatherfinalapp.db.WeatherLocalDataSourceImp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRepositoryImp (
    private val remoteSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource
) : WeatherRepository {

    companion object {
        private var repo: WeatherRepositoryImp? = null

        fun getInstance(
            remoteSource: WeatherRemoteDataSource,
            localDataSource: WeatherLocalDataSource
        ): WeatherRepositoryImp {
            if (repo == null) {
                repo = WeatherRepositoryImp(remoteSource , localDataSource )
            }
            return repo!!
        }
    }

    override fun getStoredFavLocations(): Flow<List<FavoriteLocation>> {
        return localDataSource.getAllStoredFavLocations()

    }

    override suspend fun insertLocation(location: FavoriteLocation) {
       localDataSource.addLocation(location)
    }

    override suspend fun deleteLocation(location: FavoriteLocation) {
        localDataSource.removeLocation(location)
    }


    ////Alert
    override fun getAllAlerts(): Flow<List<Alert>> {
       return localDataSource.getAllStoredAlerts()
    }

    override suspend fun insertAlert(alert: Alert) {
        localDataSource.addAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
       localDataSource.removeAlert(alert)
    }


    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        units: String?,
        lang: String?,
        appid: String?
    ): Flow<WeatherResponse> {
            return flowOf(remoteSource.makeNetworkCall(lat, lon, units, lang, appid))

    }



}
