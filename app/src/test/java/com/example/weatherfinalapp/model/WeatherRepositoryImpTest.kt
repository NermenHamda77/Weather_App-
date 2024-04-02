package com.example.weatherfinalapp.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherfinalapp.Network.FakeWeatherRemoteDataSource
import com.example.weatherfinalapp.db.FakeLocalDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class WeatherRepositoryImpTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()



    // get FavoriteLocation
    val location1 = FavoriteLocation(0.0, 0.0, "address1", 1)
    val location2 = FavoriteLocation(0.1, 0.1, "address2", 2)
    val location3 = FavoriteLocation(0.2, 0.2, "address3", 3)


    //local list
    val localSourceLocationsList = mutableListOf(
        location1,
        location2,
        location3

    )

    //remote list
    val remoteSourceResponse = WeatherResponse(
        listOf(),
        City(1, "city1", Coord(1.0, 2.0), "country1", 2, 2, 2, 2)
    )

    lateinit var fakeWeatherRemoteDataSource: FakeWeatherRemoteDataSource
    lateinit var fakeLocalDataSource: FakeLocalDataSource
    lateinit var repositoryImp: WeatherRepositoryImp
    lateinit var weather: WeatherResponse

    @Before
    fun setUp() {
        fakeLocalDataSource = FakeLocalDataSource(localSourceLocationsList)
        fakeWeatherRemoteDataSource = FakeWeatherRemoteDataSource(remoteSourceResponse)
        repositoryImp =
            WeatherRepositoryImp.getInstance(fakeWeatherRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun getStoredFavLocations() = runBlockingTest {
        //Given
        var locations = listOf<FavoriteLocation>()
        //When
        repositoryImp.getStoredFavLocations().collect {
            locations = it
        }
        //Then
        assertThat(locations, `is`(localSourceLocationsList))
    }





    @Test
    fun insertLocation() = runBlockingTest {
        //Given
        val location4 = FavoriteLocation(0.3, 0.3, "address4", 4)
        //When
        repositoryImp.insertLocation(location4)
        var favoriteLocations = listOf<FavoriteLocation>()
        repositoryImp.getStoredFavLocations().collect {
            favoriteLocations = it
        }
        //Then
        assertThat(favoriteLocations, `is`(localSourceLocationsList))

    }





    @Test
    fun deleteLocation() = runBlockingTest{
        //Given
        val location4 = FavoriteLocation(0.3, 0.3, "address4", 4)
        //When
        repositoryImp.insertLocation(location4)
        var favoriteLocations = listOf<FavoriteLocation>()
        repositoryImp.getStoredFavLocations().collect {
            favoriteLocations = it
        }
        //Then
        assertThat(favoriteLocations, `is`(localSourceLocationsList))

    }




    @Test
    fun getWeather() = runBlockingTest{
        repositoryImp.getWeather(0.0 , 0.0 , "unit" , "lang").collect{
            weather = it
        }
        assertThat(weather,`is`(remoteSourceResponse))

    }



}