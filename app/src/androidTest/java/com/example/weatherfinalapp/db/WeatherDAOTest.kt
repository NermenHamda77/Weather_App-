package com.example.weatherfinalapp.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherfinalapp.dp.getOrAwaitValue
import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.FavoriteLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.test.runBlockingTest


@SmallTest
@RunWith(AndroidJUnit4::class)
class WeatherDAOTest {
    lateinit var database: AppDataBase
    lateinit var dao :WeatherDAO


    @get:Rule
    val rule = InstantTaskExecutorRule()
    @Before
    fun setUp(){
         database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDataBase::class.java
        ).build()
        dao = database.getWeatherDAO()
    }

    @After
    fun tearDown(){
        database.close()
    }


    @Test
    fun getAllLocations_locationWith_theSameLocation() = runBlocking {
            val favoriteLocation1 = FavoriteLocation(0.0,0.0,"address",1)
            dao.insertLocation(favoriteLocation1)
            val favoriteLocation2 = FavoriteLocation(1.0,11.0,"address",2)
            dao.insertLocation(favoriteLocation2)
            val favoriteLocation3 = FavoriteLocation(2.0,12.0,"address",3)
            dao.insertLocation(favoriteLocation3)

            val result = dao.getAllLocations().getOrAwaitValue()

            assertThat(result, not(nullValue()))
            val collection = listOf(favoriteLocation1,favoriteLocation2, favoriteLocation3)
            assertThat(result , `is`(collection))

    }


    @Test
    fun insertLocation_theSameLocation() = runBlockingTest {
        val favoriteLocation1 = FavoriteLocation(0.0,0.0,"address",1)
        dao.insertLocation(favoriteLocation1)
        val favoriteLocation2 = FavoriteLocation(1.0,11.0,"address",2)
        dao.insertLocation(favoriteLocation2)
        val favoriteLocation3 = FavoriteLocation(2.0,12.0,"address",3)
        dao.insertLocation(favoriteLocation3)

        val result = dao.getAllLocations().getOrAwaitValue()
        val collection = listOf(favoriteLocation1,favoriteLocation2, favoriteLocation3)

        // Then
        assertThat(result, not(nullValue()))
        assertThat(result , `is`(collection))

    }

    @Test
    fun deleteLocation_theSameLocation() = runBlockingTest {
        //Given

        val favoriteLocation1 = FavoriteLocation(0.0,0.0,"address",1)
        dao.insertLocation(favoriteLocation1)
        val favoriteLocation2 = FavoriteLocation(1.0,11.0,"address",2)
        dao.insertLocation(favoriteLocation2)
        val favoriteLocation3 = FavoriteLocation(2.0,12.0,"address",3)
        dao.insertLocation(favoriteLocation3)

        dao.deleteLocation(favoriteLocation1)


        //When
        val result = dao.getAllLocations().getOrAwaitValue()
        val collection = listOf(favoriteLocation2, favoriteLocation3)

        // Then
        assertThat(result, not(nullValue()))
        assertThat(result , `is`(collection))

    }




    //////////////////          Alert

    @Test
    fun getAllAlerts_alerts_theSameAlert() = runBlocking {
        //Given
        val alert1 = Alert(1,"2/2","08:00" , "alarm" , 1.0,1.0,"address1")
        val alert2 = Alert(2,"4/2","05:00", "notification" , 0.0,0.0,"address2")
        val alert3 = Alert(3,"3/2","06:00", "alarm" , 2.0,2.0,"address3")
        dao.insertAlert(alert1)
        dao.insertAlert(alert2)
        dao.insertAlert(alert3)


        // When
        val result = dao.getAllAlerts().getOrAwaitValue()
        val collection = listOf(alert1, alert2, alert3)
        // Then
        assertThat(result, not(nullValue()))
        assertThat(result, `is`(collection))


    }


    @Test
    fun insertAlert_alerts_theSameAlert() = runBlockingTest {
        //Given
        val alert1 = Alert(1,"2/2","08:00", "alarm" , 1.0,1.0,"address1")
        val alert2 = Alert(2,"4/2","05:00" ,"notification" , 0.0,0.0,"address2")
        val alert3 = Alert(3,"3/2","06:00", "alarm" , 2.0,2.0,"address3")
        dao.insertAlert(alert1)
        dao.insertAlert(alert2)
        dao.insertAlert(alert3)


        // When
        val result = dao.getAllAlerts().getOrAwaitValue()
        val collection = listOf(alert1, alert2, alert3)
        // Then
        assertThat(result, not(nullValue()))
        assertThat(result, `is`(collection))


    }



    @Test
    fun deleteAlert_alerts_theSameAlert() = runBlockingTest {
        //Given
        val alert1 = Alert(1,"2/2","08:00","alarm" , 1.0,1.0,"address1")
        val alert2 = Alert(2,"4/2","05:00","notification" , 0.0,0.0,"address2")
        val alert3 = Alert(3,"3/2","06:00", "alarm" , 2.0,2.0,"address3")
        dao.insertAlert(alert1)
        dao.insertAlert(alert2)
        dao.insertAlert(alert3)

        dao.deleteAlert(alert1)


        // When
        val result = dao.getAllAlerts().getOrAwaitValue()
        val collection =  listOf(alert2, alert3)

        // Then
         assertThat(result, not(nullValue()))
         assertThat(result, `is`(collection))
    }

}