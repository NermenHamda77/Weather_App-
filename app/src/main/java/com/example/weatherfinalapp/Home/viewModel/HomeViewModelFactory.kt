package com.example.weatherfinalapp.Home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.Home.viewModel.HomeViewModel
import com.example.weatherfinalapp.Settings.view.SharedPreferencesManager
import com.example.weatherfinalapp.model.WeatherRepository

class HomeViewModelFactory (private val repo: WeatherRepository, private val sharedPreferencesManager: SharedPreferencesManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repo , sharedPreferencesManager) as T

        } else {
            throw IllegalArgumentException("viewModel Class Not Found")
        }
    }
}
