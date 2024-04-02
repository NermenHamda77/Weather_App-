package com.example.weatherfinalapp.Favorite.viewModel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherfinalapp.getOrAwaitValue
import com.example.weatherfinalapp.model.FakeWeatherRepository
import com.example.weatherfinalapp.model.FavoriteLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi

class FavLocationViewModelTest {
    private lateinit var viewModel: FavLocationViewModel
    private lateinit var repository: FakeWeatherRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    @Before
    fun setUp() {
        repository = FakeWeatherRepository()
        viewModel = FavLocationViewModel(repository)

    }

    @Test
    fun getLocalLocations_returnFavLocations() {

        //When
        val result = viewModel.location.getOrAwaitValue { }

        //Return
        assertThat(result, not(nullValue()))
    }

    @Test
    fun removeLocationFromFav() = runBlocking {
        //Given
        val location1 = FavoriteLocation(0.0, 0.0, "address1", 1)
        repository.insertLocation(location1)

        val location2 = FavoriteLocation(0.1, 0.1, "address2", 2)
        repository.insertLocation(location2)

        repository.deleteLocation(location1)

        //When
        val list = listOf(location2)

        val result = viewModel.location.getOrAwaitValue { }

        //Return
        assertThat(result, not(nullValue()))
        assertThat(result, `is`(list))


    }
}

