package com.example.weatherfinalapp.LocationDetailsScreen.view

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherfinalapp.LocationDetailsScreen.viewModel.LocationDetailsViewModel
import com.example.weatherfinalapp.LocationDetailsScreen.viewModel.LocationDetailsViewModelFactory
import com.example.weatherfinalapp.Network.WeatherRemoteDataSourceImp
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.Settings.view.SharedPreferencesManager
import com.example.weatherfinalapp.db.WeatherLocalDataSourceImp
import com.example.weatherfinalapp.model.ApiWeather
import com.example.weatherfinalapp.model.FavoriteLocation
import com.example.weatherfinalapp.model.WeatherRepositoryImp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val TAG = "LocationDetailsActivity"
class LocationDetailsActivity : AppCompatActivity() {
    private lateinit var city: TextView
    private lateinit var date: TextView
    private lateinit var description: TextView
    private lateinit var temp: TextView
    private lateinit var humidity: TextView
    private lateinit var cloud: TextView
    private lateinit var pressure: TextView
    private lateinit var wind: TextView
    private lateinit var image: ImageView

    private lateinit var viewModel: LocationDetailsViewModel
    private lateinit var locationDetailsViewModelFactory: LocationDetailsViewModelFactory
    private lateinit var hoursLocationDetailsAdapter: HoursLocationDetailsAdapter
    private lateinit var weekLocationDetailsAdapter: WeekLocationDetailsAdapter

    private lateinit var hourRecyclerView: RecyclerView
    private lateinit var weekRecyclerView: RecyclerView

    private lateinit var hourLinearLayoutManager: LinearLayoutManager
    private lateinit var weekLinearLayoutManager: LinearLayoutManager


    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    lateinit var geocoder: Geocoder
    var latit :  Double = 0.0
    var longit : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        city = findViewById(R.id.tv_city_name_loc_dets)
        date = findViewById(R.id.tv_date_top_home_loc_dets)
        description = findViewById(R.id.tv_city_state_loc_dets)
        temp = findViewById(R.id.tv_city_temp_loc_dets)
        image = findViewById(R.id.iv_city_state_loc_dets)
        cloud = findViewById(R.id.tv_cloud_value_loc_dets)
        humidity = findViewById(R.id.tv_humidity_value_loc_dets)
        wind = findViewById(R.id.tv_wind_value_loc_dets)
        pressure = findViewById(R.id.tv_pressure_value_loc_dets)



        hourRecyclerView = findViewById(R.id.rv_day_hourly_state_loc_dets)
        weekRecyclerView = findViewById(R.id.rv_week_state_loc_dets)

        hourLinearLayoutManager = LinearLayoutManager(this)
        hourLinearLayoutManager.orientation = RecyclerView.HORIZONTAL
        hoursLocationDetailsAdapter = HoursLocationDetailsAdapter(this)

        hourRecyclerView.adapter = hoursLocationDetailsAdapter
        hourRecyclerView.layoutManager = hourLinearLayoutManager


        weekLinearLayoutManager = LinearLayoutManager(this)
        weekLinearLayoutManager.orientation = RecyclerView.VERTICAL

        weekLocationDetailsAdapter = WeekLocationDetailsAdapter(this)

        weekRecyclerView.adapter = weekLocationDetailsAdapter
        weekRecyclerView.layoutManager = weekLinearLayoutManager
        sharedPreferencesManager = SharedPreferencesManager(this)

        locationDetailsViewModelFactory = LocationDetailsViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp.getInstance(this)
            ), sharedPreferencesManager
        )

        geocoder = Geocoder(this)


        viewModel = ViewModelProvider(
            this,
            locationDetailsViewModelFactory
        ).get(LocationDetailsViewModel::class.java)


        val favoriteLocation = intent.getParcelableExtra<FavoriteLocation>("favorite_location")


        if (favoriteLocation != null) {
            latit = favoriteLocation.latitude
            longit = favoriteLocation.longtiude

        }

      //  val address = getAddress(latit, longit)

       // val favLocation = FavoriteLocation(latit, longit, address)

        lifecycleScope.launch {
            viewModel.getAllWeathersOfLoc(latit , longit  )
        }


        lifecycleScope.launch {
            viewModel._weather.collectLatest { result ->
                when(result){
                    is ApiWeather.Loading -> {


                        // recyclerView.visibility = View.GONE

                    }
                    is ApiWeather.Success -> {

                        val weather = result.data
                        city.text = weather.city.name

                        hourRecyclerView.visibility = View.VISIBLE
                        weekRecyclerView.visibility = View.VISIBLE

                        // date.text =
                        Glide.with(this@LocationDetailsActivity)
                            .load("https://openweathermap.org/img/wn/${weather.list[0].weather[0].icon}@2x.png")
                            .into(image)



                        val temperatureFahrenheit = weather.list.firstOrNull()?.main?.temp
                        val temperatureCelsuis = (temperatureFahrenheit?.minus(273.15))
                        val temperatureFormatted = String.format("%.2f" , temperatureCelsuis)
                        temp.text = "$temperatureFormatted°c"



                        description.text = weather.list.firstOrNull()?.weather?.firstOrNull()?.description.toString()
                        humidity.text = "${weather.list.firstOrNull()?.main?.humidity.toString()}%"
                        cloud.text = "${weather.list.firstOrNull()?.clouds?.all.toString()}%"
                        pressure.text = "${weather.list.firstOrNull()?.main?.pressure.toString()}hpa"
                        wind.text = "${weather.list.firstOrNull()?.wind?.speed.toString()}m/s"


                        hoursLocationDetailsAdapter.setList(weather.list)
                        weekLocationDetailsAdapter.setList(weather.list)
                        updateWindUI(weather.list.firstOrNull()?.wind?.speed ?: 0.0)


                        // Get the current date
                        val currentDate = Calendar.getInstance().time

                        // Define the date format
                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                        // Format the current date
                        val formattedDate = dateFormat.format(currentDate)

                        // Set the formatted date to the TextView
                        date.text = formattedDate



                        /*val text = result.data.list.get(0).dt
                        Log.i(TAG, "onCreateView:${text} ")*/

                    }
                    else ->
                    {
                        Log.i(TAG, "onCreateView: error")

                        //   Toast.makeText(this@AllProductActivity,"product", Toast.LENGTH_SHORT).show()

                    }

                }
            }

        }

    }


    private fun updateWindUI(speed: Double) {
        val windValueTextView = findViewById<TextView>(R.id.tv_wind_value_loc_dets)
        val windUnit = sharedPreferencesManager.getSelectedWindUnit()

        val convertedSpeed = convertWindSpeed(speed, windUnit)

        windValueTextView?.text = "$convertedSpeed $windUnit"
    }

    private fun convertWindSpeed(speed: Double, windUnit: String): Double {
        return when (windUnit) {
            SharedPreferencesManager.DEFAULT_WIND_UNIT -> speed
            "Mile/Hr" -> speed * 2.23694
            else -> speed
        }
    }






}
