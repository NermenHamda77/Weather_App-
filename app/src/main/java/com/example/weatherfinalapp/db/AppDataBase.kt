package com.example.weatherfinalapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.FavoriteLocation

@Database(entities = arrayOf(FavoriteLocation::class, Alert::class), version = 2)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getWeatherDAO (): WeatherDAO
    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null
        fun getInstance(ctx: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(ctx.applicationContext , AppDataBase::class.java, "weather_database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}