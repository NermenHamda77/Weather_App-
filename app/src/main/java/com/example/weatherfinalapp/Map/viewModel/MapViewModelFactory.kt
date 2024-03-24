package com.example.weatherfinalapp.Map.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherfinalapp.model.WeatherRepository

class MapViewModelFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            MapViewModel(repo) as T

        } else {
            throw IllegalArgumentException("viewModel Class Not Found")
        }
    }
}

/*
lass HomeViewModelFactory (private val repo: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repo) as T

        } else {
            throw IllegalArgumentException("viewModel Class Not Found")
        }
    }
}

 */