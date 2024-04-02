package com.example.weatherfinalapp.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherfinalapp.dp.getOrAwaitValue
import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.FavoriteLocation
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert

@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceImpTest {
     private lateinit var database: AppDataBase
     private lateinit var localDataSourceImp: WeatherLocalDataSourceImp


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = MainRule()


    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).allowMainThreadQueries().build()
        localDataSourceImp = WeatherLocalDataSourceImp(ApplicationProvider.getApplicationContext())
    }

    @After
    fun tearDown() {
        database.close()
    }



    @Test
    fun addLocation_theSameLocation() = runBlockingTest {        //done
        //Given
        val favorite1 = FavoriteLocation(0.0,0.0,"address" , 1)

        localDataSourceImp.addLocation(favorite1)



        val collection = listOf(favorite1)

        //When

        val result = localDataSourceImp.getAllStoredFavLocations().getOrAwaitValue()

        // Then
        assertThat(result, not(nullValue()))
        assertThat(result , `is`(collection))

    }


    @Test
    fun removeLocation_location_returnLocation() = runBlockingTest {     //done
        //given
        val favoriteLocation1 = FavoriteLocation(0.0,0.0,"address",1)
        val favoriteLocation2 = FavoriteLocation(5.0, 8.0, "address2",2 )

        localDataSourceImp.addLocation(favoriteLocation1)
        localDataSourceImp.addLocation(favoriteLocation2)

        localDataSourceImp.removeLocation(favoriteLocation2)


        //when

        val result = localDataSourceImp.getAllStoredFavLocations().getOrAwaitValue()
        //then
        assertThat(result, `is`(listOf(favoriteLocation1)))
    }







    @Test
    fun getAllStoredFavLocations_allLocations() = runBlockingTest  {
        //Given
        val favorite1 = FavoriteLocation(0.0,0.0,"address" , 1)
        val favorite2 = FavoriteLocation(5.0, 8.0, "address2",3)
        localDataSourceImp.addLocation(favorite1)
        localDataSourceImp.addLocation(favorite2)



        val collection = listOf(favorite1, favorite2)

        //When

        val result = localDataSourceImp.getAllStoredFavLocations().getOrAwaitValue()

        // Then
        assertThat(result, not(nullValue()))
        assertThat(result , `is`(collection))


    }

    //// Alert
    @Test
    fun addAlert_theSameAlert() = runBlockingTest {
        //Given
        val alert1 = Alert(1,"2/2","08:00", "alarm" , 1.0,1.0,"address1")
        localDataSourceImp.addAlert(alert1)

        //When
        val collection = listOf(alert1)

        val result = localDataSourceImp.getAllStoredAlerts().getOrAwaitValue()

        // Then
       // assertThat(result, not(nullValue()))
        assertThat(result , `is`(collection))

    }


    @Test
    fun removeAlert_Alert_returnAlert() = runBlockingTest {
        //given
        val alert1 = Alert(1,"2/2","08:00","alarm" , 1.0,1.0,"address1")

        val alert2 = Alert(2,"2/2","08:00","alarm" , 1.0,1.0,"address1")
        localDataSourceImp.addAlert(alert1)

        localDataSourceImp.addAlert(alert2)

        localDataSourceImp.removeAlert(alert2)


        //when
        val collection = listOf(alert2)
        val result = localDataSourceImp.getAllStoredAlerts().getOrAwaitValue()
        //then
        assertThat(result, `is`(collection))
    }


    @Test
    fun getAllStoredAlerts_alerts() = runBlocking {
        //Given
        val alert1 = Alert(2, "2/2", "08:00",  "alarm", 1.0, 1.0, "address1")
        localDataSourceImp.addAlert(alert1)

        val alert2 = Alert(3, "2/2", "08:00", "alarm", 1.0, 1.0, "address1")
        localDataSourceImp.addAlert(alert2)


        //When
        val collection = listOf<Alert>(alert1)

        val result = localDataSourceImp.getAllStoredAlerts().getOrAwaitValue()

        // Then
        assertThat(result, not(nullValue()))
        assertThat(result, `is`(collection))


    }

    @Test
    fun getAllAlerts_returnAlerts()=rule.runBlockingTest {

        //Given
        val list = listOf(Alert(1, "2/2", "08:00", "alarm", 1.0, 1.0, "address1"),
            Alert(2, "2/2", "08:00", "alarm", 1.0, 1.0, "address1"))
        val alert1 = Alert(1, "2/2", "08:00", "alarm", 1.0, 1.0, "address1")
        val alert3 = Alert(2, "2/2", "08:00", "alarm", 1.0, 1.0, "address1")

        //When
        localDataSourceImp.addAlert(alert3)
        localDataSourceImp.addAlert(alert1)

        var result = listOf<Alert>()
        result = localDataSourceImp.getAllStoredAlerts().first()
        //Return
        Assert.assertThat(result, `is`(list))
    }

}