package com.example.weatherfinalapp.Network

import android.util.Log
import com.example.weatherfinalapp.model.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "WeatherRemoteDataSource"
class WeatherRemoteDataSourceImp  private constructor(){
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


    suspend fun makeNetworkCall(
        lat : Double? ,
        lon : Double? ,
        units : String? = "metric",
        lang : String? = "en" ,
        appid : String? = "c687f9659a452c95a84f05c506629873"  
    ): WeatherResponse{
        return withContext(Dispatchers.IO) {
            weatherServices.getCurrentWeather(lat, lon, units, lang, appid)
        }
    }


}

/*
class ProductsRemoteDataSourceImp private constructor() {
    private val TAG = "RESPONSE"
    private val BASE_URL = "https://dummyjson.com/"
    private var productService: ProductService

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        productService = retrofit.create(ProductService::class.java)
    }

    companion object {
        private var client: ProductsRemoteDataSourceImp? = null

        fun getInstance(): ProductsRemoteDataSourceImp {
            if (client == null) {
                client = ProductsRemoteDataSourceImp()
            }
            return client as ProductsRemoteDataSourceImp
        }
    }

    suspend fun makeNetworkCall(): ProductResponse {
        return withContext(Dispatchers.IO) {
            productService.getProducts()
        }
    }
}

 */