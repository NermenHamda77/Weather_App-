package com.example.weatherfinalapp.Settings.view

import android.content.Context

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)

    companion object {
        const val DEFAULT_WIND_UNIT = "Meter/Sec"
        const val KEY_SELECTED_WIND_UNIT = "selected_wind_unit"
    }

    fun saveSelectedWindUnit(unit: String) {
        sharedPreferences.edit().putString(KEY_SELECTED_WIND_UNIT, unit).apply()
    }

    fun getSelectedWindUnit(): String {
        return sharedPreferences.getString(KEY_SELECTED_WIND_UNIT, DEFAULT_WIND_UNIT) ?: DEFAULT_WIND_UNIT
    }

    fun saveSelectedUnit(unit: String) {
        sharedPreferences.edit().putString("selected_unit", unit).apply()
    }

    fun getSelectedUnit(): String {
        return sharedPreferences.getString("selected_unit", "metric") ?: "metric"
    }

    fun saveSelectedLanguage(language: String) {
        sharedPreferences.edit().putString("selected_language", language).apply()
    }

    fun getSelectedLanguage(): String {
        return sharedPreferences.getString("selected_language", "en") ?: "en"
    }

    fun saveSelectedLocation(location: String) {
        sharedPreferences.edit().putString("selected_location", location).apply()
    }

    fun getSelectedLocation(): String {
        return sharedPreferences.getString("selected_location", "GPS") ?: "GPS"
    }
}