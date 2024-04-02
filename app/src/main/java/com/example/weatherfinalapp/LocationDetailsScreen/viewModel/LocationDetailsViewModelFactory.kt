package com.example.weatherfinalapp.LocationDetailsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.Home.viewModel.HomeViewModel
import com.example.weatherfinalapp.Settings.view.SharedPreferencesManager
import com.example.weatherfinalapp.model.WeatherRepository

class LocationDetailsViewModelFactory (private val repo: WeatherRepository, private val sharedPreferencesManager: SharedPreferencesManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationDetailsViewModel::class.java)) {
            LocationDetailsViewModel(repo, sharedPreferencesManager) as T

        } else {
            throw IllegalArgumentException("viewModel Class Not Found")
        }
    }
}
