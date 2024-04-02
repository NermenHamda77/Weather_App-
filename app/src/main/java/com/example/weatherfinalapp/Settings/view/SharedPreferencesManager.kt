package com.example.weatherfinalapp.Settings.view

import android.content.Context

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)


    fun saveSelectedWind(unit: String) {
        sharedPreferences.edit().putString("selected_wind", unit).apply()
    }

    fun getSelectedWind(): String {
        return sharedPreferences.getString("selected_wind", "metric") ?: "metric"   ////?????????
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
}