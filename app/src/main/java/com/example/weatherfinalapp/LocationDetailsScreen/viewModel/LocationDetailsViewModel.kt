package com.example.weatherfinalapp.LocationDetailsScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherfinalapp.model.ApiWeather
import com.example.weatherfinalapp.model.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

//////?????????????
// i think i need function that take location and get the weather of it

class LocationDetailsViewModel(private val repo: WeatherRepository) : ViewModel()  {
    val _weather: MutableStateFlow<ApiWeather> = MutableStateFlow(ApiWeather.Loading)
    suspend fun getAllWeathersOfLoc(lat: Double, lon: Double, lang: String, units: String) {
        viewModelScope.launch {
            repo.getWeather(lat, lon, units, lang)
                .catch { e ->
                    _weather.value = ApiWeather.Failure(e)
                }
                .collect { weatherResponse ->
                    _weather.value = ApiWeather.Success(weatherResponse)
                }
        }


    }
}


/*
class HomeViewModel(private val repo: WeatherRepository) : ViewModel() {

   val _weather: MutableStateFlow<ApiWeather> = MutableStateFlow(ApiWeather.Loading)
/*   private val _weather = MutableLiveData<WeatherResponse>()

    val weatherList: LiveData<WeatherResponse> = _weather*/



        suspend fun getAllWeathers(lat: Double, lon: Double, lang: String, units: String) {
            viewModelScope.launch {
                repo.getWeather(lat, lon, units, lang)
                    .catch { e ->
                        _weather.value = ApiWeather.Failure(e)
                    }
                    .collect { weatherResponse ->
                        _weather.value = ApiWeather.Success(weatherResponse)
                    }
            }


    }
 */