package com.example.weatherfinalapp.Alert.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherapp.alert.view.AlertViewModel
import com.example.weatherfinalapp.Favorite.viewModel.FavLocationViewModel
import com.example.weatherfinalapp.getOrAwaitValue
import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.FakeWeatherRepository
import com.example.weatherfinalapp.model.FavoriteLocation
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AlertViewModelTest {

    private lateinit var viewModel: AlertViewModel
    private lateinit var repository: FakeWeatherRepository

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()



    @Before
    fun setUp() {
        repository = FakeWeatherRepository()
        viewModel = AlertViewModel(repository)
    }


    @Test
    fun getLocalAlerts_returnAlerts() {

        //When
        val result = viewModel.alert.getOrAwaitValue { }

        //Return
        assertThat(result, not(nullValue()))
    }

    @Test
    fun removeAlertFromList() = runBlocking {
        //Given
        val alert1 = Alert(1,"2/2","08:00","alarm" , 1.0,1.0,"address1")
        val alert2 = Alert(2,"4/2","05:00", "notification" , 0.0,0.0,"address2")
        val alert3 = Alert(3,"3/2","06:00", "alarm" , 2.0,2.0,"address3")
        repository.insertAlert(alert1)


        repository.insertAlert(alert2)

        repository.insertAlert(alert3)
        repository.deleteAlert(alert3)

        //When
        val list = listOf(alert1,alert2)

        val result = viewModel.alert.getOrAwaitValue { }

        //Return
        assertThat(result, not(nullValue()))
        assertThat(result, `is`(list))


    }


    @Test
    fun addAlertToList() = runBlocking {
        //Given
        val alert1 = Alert(1,"2/2","08:00","alarm" , 1.0,1.0,"address1")
        val alert2 = Alert(2,"4/2","05:00","notification" , 0.0,0.0,"address2")
        val alert3 = Alert(3,"3/2","06:00", "alarm" , 2.0,2.0,"address3")
        repository.insertAlert(alert1)


        repository.insertAlert(alert2)

        repository.insertAlert(alert3)

        //When
        val list = listOf(alert1,alert2 , alert3)

        val result = viewModel.alert.getOrAwaitValue { }

        //Return
        assertThat(result, not(nullValue()))
        assertThat(result, `is`(list))


    }

}