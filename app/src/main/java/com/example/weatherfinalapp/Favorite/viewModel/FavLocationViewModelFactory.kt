package com.example.weatherfinalapp.Favorite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherfinalapp.model.WeatherRepository

class FavLocationViewModelFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  if (modelClass.isAssignableFrom(FavLocationViewModel::class.java)) {
            FavLocationViewModel(repo) as T

        } else {
            throw IllegalArgumentException("viewModel Class Not Found")
        }
    }
}

