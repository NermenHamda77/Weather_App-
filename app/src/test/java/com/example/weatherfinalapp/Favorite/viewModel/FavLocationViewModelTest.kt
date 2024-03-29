package com.example.weatherfinalapp.Favorite.viewModel

import org.junit.Test


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherfinalapp.model.FavoriteLocation
import com.example.weatherfinalapp.model.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@ExperimentalCoroutinesApi
class FavLocationViewModelTest {
    private lateinit var viewModel: FavLocationViewModel
    private lateinit var repository: WeatherRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        repository = mock(WeatherRepository::class.java)
        viewModel = FavLocationViewModel(repository)
    }

    @Test
    fun testGetLocalLocations() {
        viewModel.getLocalLocations()
        // verify that the repository method is called
        verify(repository).getStoredFavLocations()
    }

   /* @Test
    fun testRemoveLocationFromFav() {
        val location = FavoriteLocation() // create a FavoriteLocation object
        viewModel.removeLocationFromFav(location)
        // verify that the repository method is called
        verify(repository).deleteLocation(location)
    }*/

    // you can add more test methods for other functions as needed
}