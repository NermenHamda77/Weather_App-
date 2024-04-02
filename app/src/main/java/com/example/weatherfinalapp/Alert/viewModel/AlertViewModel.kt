package com.example.weatherapp.alert.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.ApiWeather
import com.example.weatherfinalapp.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel(private val _repository: WeatherRepository) : ViewModel()  {
    private var _alert: MutableLiveData<List<Alert>> = MutableLiveData<List<Alert>>()
    val alert: LiveData<List<Alert>> get() = _alert
    val _weather: MutableStateFlow<ApiWeather> = MutableStateFlow(ApiWeather.Loading)

    init {
        getLocalAlerts()
    }

    suspend fun getAllWeathersOfLoc(lat: Double, lon: Double, lang: String, units: String) {
        viewModelScope.launch {
            _repository.getWeather(lat, lon, units, lang)
                .catch { e ->
                    _weather.value = ApiWeather.Failure(e)
                }
                .collect { weatherResponse ->
                    _weather.value = ApiWeather.Success(weatherResponse)
                }
        }


    }

    fun getLocalAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.getAllAlerts().collect {
                _alert.postValue(it)
            }
        }

    }

    fun addAlertToList(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.insertAlert(alert)
            getLocalAlerts()
        }
    }

    fun removeAlertFromList(alert: Alert) {
        viewModelScope.launch(Dispatchers.IO) {
            _repository.deleteAlert(alert)
            getLocalAlerts()
        }
    }


}

