package com.example.weatherfinalapp.model

sealed class ApiWeather {
    data class Success(val data: WeatherResponse) : ApiWeather()
    data class Failure(val msg : Throwable) : ApiWeather()
    data object Loading :ApiWeather()
}