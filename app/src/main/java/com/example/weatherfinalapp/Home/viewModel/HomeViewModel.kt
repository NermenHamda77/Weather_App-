package com.example.weatherapp.Home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherfinalapp.Settings.view.SharedPreferencesManager
import com.example.weatherfinalapp.model.ApiWeather
import com.example.weatherfinalapp.model.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: WeatherRepository , private val sharedPreferencesManager: SharedPreferencesManager) : ViewModel() {

    val _weather: MutableStateFlow<ApiWeather> = MutableStateFlow(ApiWeather.Loading)



    suspend fun getAllWeathers(lat: Double, lon: Double) {
        val selectedUnit = getSelectedUnit()
        val selectedLanguage = getSelectedLanguage()
        Log.i("languge", "getCurrentWeather: "+selectedLanguage)


        viewModelScope.launch {
            repo.getWeather(lat, lon, selectedUnit, selectedLanguage)
                .catch { e ->
                    _weather.value = ApiWeather.Failure(e)
                }
                .collect { weatherResponse ->
                    _weather.value = ApiWeather.Success(weatherResponse)
                }
        }


    }

    private fun getSelectedUnit(): String {
        return sharedPreferencesManager.getSelectedUnit()
    }
    private fun getSelectedLanguage(): String {
        return sharedPreferencesManager.getSelectedLanguage()
    }
}