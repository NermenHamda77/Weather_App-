package com.example.weatherfinalapp.Favorite.view

import com.example.weatherfinalapp.model.FavoriteLocation

interface OnFavClickListener {
    fun deleteLocation(location : FavoriteLocation )
    fun showFavLocationDetails(location : FavoriteLocation )

}