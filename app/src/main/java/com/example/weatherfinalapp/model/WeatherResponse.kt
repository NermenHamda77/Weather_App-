package com.example.weatherfinalapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "weathers")
data class WeatherResponse(

    val list: List<ListWeather>,

    @PrimaryKey
    val city : City

)