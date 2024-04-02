package com.example.weatherfinalapp.Network

import android.util.Log
import com.example.weatherfinalapp.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "WeatherRemoteDataSource"
class WeatherRemoteDataSourceImp  private constructor() : WeatherRemoteDataSource {
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private var weatherServices : WeatherServices

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        weatherServices = retrofit.create(WeatherServices::class.java)
    }

    companion object {
        private var client: WeatherRemoteDataSourceImp? = null

        fun getInstance(): WeatherRemoteDataSourceImp {
            if (client == null) {
                client = WeatherRemoteDataSourceImp()
                Log.i(TAG, "getInstance: done")
            }
            return client as WeatherRemoteDataSourceImp
        }
    }


    override suspend fun makeNetworkCall(
        lat : Double?,
        lon : Double?,
        units : String?,
        lang : String?,
        appid : String?
    ): WeatherResponse{
        return withContext(Dispatchers.IO) {
            weatherServices.getCurrentWeather(lat, lon, units, lang, appid)
        }
    }


}
