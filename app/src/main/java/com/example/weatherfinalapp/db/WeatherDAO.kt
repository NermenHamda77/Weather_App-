package com.example.weatherfinalapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.FavoriteLocation
import com.example.weatherfinalapp.model.Weather
import com.example.weatherfinalapp.model.WeatherResponse
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDAO {

        /// Favorite
        @Query("SELECT * FROM favoriteLocation")
        fun  getAllLocations(): Flow<List<FavoriteLocation>>
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertLocation(location: FavoriteLocation): Long
        @Delete
        suspend fun deleteLocation(location: FavoriteLocation)



        ////// Alert
        @Query("SELECT * FROM alert")
        fun  getAllAlerts(): Flow<List<Alert>>
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertAlert(alert: Alert): Long
        @Delete
        suspend fun deleteAlert(alert: Alert)

}