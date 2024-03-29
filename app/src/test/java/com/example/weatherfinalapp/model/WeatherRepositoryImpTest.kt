package com.example.weatherfinalapp.model


class WeatherRepositoryImpTest {



    // get FavoriteLocation
    val location1 = FavoriteLocation(0.0 ,0.0,"address1")
    val location2 = FavoriteLocation(0.1 ,0.1,"address2")
    val location3 = FavoriteLocation(0.2 ,0.2,"address3")
    val location4 = FavoriteLocation(0.3 ,0.3,"address4")


    //local list of fav location
    val localList = listOf(
        location1,
        location2
    )
    //remote list of fav location
    val remoteList = listOf(
        location3,
        location4
    )





    fun getStoredFavLocations() {
    }

    fun insertLocation() {
    }

    fun deleteLocation() {
    }

    fun getWeather() {
    }
}