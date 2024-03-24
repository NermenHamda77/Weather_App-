package com.example.weatherfinalapp.LocationDetailsScreen.view

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherfinalapp.LocationDetailsScreen.viewModel.LocationDetailsViewModel
import com.example.weatherfinalapp.LocationDetailsScreen.viewModel.LocationDetailsViewModelFactory
import com.example.weatherfinalapp.Network.WeatherRemoteDataSourceImp
import com.example.weatherfinalapp.R
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


        locationDetailsViewModelFactory = LocationDetailsViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp.getInstance(this)
            )
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
            viewModel.getAllWeathersOfLoc(latit , longit , "en" , "fer" )
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

                        temp.text = "${weather.list.firstOrNull()?.main?.temp.toString()}°C"
                        description.text = weather.list.firstOrNull()?.weather?.firstOrNull()?.description.toString()
                        humidity.text = "${weather.list.firstOrNull()?.main?.temp.toString()}%"
                        cloud.text = "${weather.list.firstOrNull()?.clouds?.all.toString()}%"
                        pressure.text = "${weather.list.firstOrNull()?.main?.temp.toString()}hpa"
                        wind.text = "${weather.list.firstOrNull()?.wind?.speed.toString()}m/s"


                        hoursLocationDetailsAdapter.setList(weather.list)
                        weekLocationDetailsAdapter.setList(weather.list)


                        ///???????????? i should to use api to show 12 hours of the day

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

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_location_details)

    // Initialize other views and variables...

    lifecycleScope.launch {
        viewModel.getAllWeathers(latit, longit, "en", "fer")

        viewModel._weather.collectLatest { result ->
            when (result) {
                is ApiWeather.Loading -> {
                    // Handle loading state if needed
                }
                is ApiWeather.Success -> {
                    val weather = result.data

                    // Access city and other properties here
                    city.text = weather.city.name
                    // Other property assignments...
                    description.text = weather.list.firstOrNull()?.weather?.firstOrNull()?.description.toString()
                    temp.text = "${weather.list.firstOrNull()?.main?.temp.toString()}°C"
                    humidity.text = "${weather.list.firstOrNull()?.main?.humidity.toString()}%"
                    cloud.text = "${weather.list.firstOrNull()?.clouds?.all.toString()}%"
                    pressure.text = "${weather.list.firstOrNull()?.main?.pressure.toString()}hpa"
                    wind.text = "${weather.list.firstOrNull()?.wind?.speed.toString()}m/s"

                    Glide.with(this@LocationDetailsActivity)
                        .load("https://openweathermap.org/img/wn/${weather.list[0].weather[0].icon}@2x.png")
                        .into(image)

                    // Set RecyclerView adapters
                    hoursLocationDetailsAdapter.setList(weather.list)
                    weekLocationDetailsAdapter.setList(weather.list)

                    // Set the current date
                    val currentDate = Calendar.getInstance().time
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(currentDate)
                    date.text = formattedDate
                }
                else -> {
                    // Handle error state if needed
                }
            }
        }
    }
}

     */


    /*private fun getAddress(latitude: Double, longitude: Double): String? {
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses?.isNotEmpty() == true) {
                addresses[0].getAddressLine(0) // You can choose any part of the address you need
            } else {
                return null
            }
        } catch (e: Exception) {
            Log.e("MapActivity", "Error getting address", e)
            "Error getting address. Please try again later."
        }
    }*/

}

/*
  private lateinit var city : TextView
    private lateinit var date : TextView
    private lateinit var description : TextView
    private lateinit var temp : TextView
    private lateinit var humidity : TextView
    private lateinit var cloud : TextView
    private lateinit var pressure : TextView
    private lateinit var wind : TextView
    private lateinit var image : ImageView

    private lateinit var viewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
//    private lateinit var hourHomeAdapter: HourHomeAdapter
    private lateinit var hourHomeAdapter: HourHomeAdapter1
//    private lateinit var weekHomeAdapter: WeekHomeAdapter
private lateinit var weekHomeAdapter: WeekHomeAdapter1

    private lateinit var hourRecyclerView: RecyclerView
    private lateinit var weekRecyclerView: RecyclerView

    private lateinit var hourLinearLayoutManager: LinearLayoutManager
    private lateinit var weekLinearLayoutManager: LinearLayoutManager

    lateinit var geocoder: Geocoder
    var locationRequestID = 1

    var latit :  Double = 0.0
    var longit : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)


        city = view.findViewById(R.id.tv_city_name_loc_dets)
        date = view.findViewById(R.id.tv_date_top_home_loc_dets)
        description = view.findViewById(R.id.tv_city_state_loc_dets)
        temp = view.findViewById(R.id.tv_city_temp_loc_dets)
        image = view.findViewById(R.id.iv_city_state_loc_dets)


        cloud = view.findViewById(R.id.tv_cloud_value_loc_dets)
        humidity = view.findViewById(R.id.tv_humidity_value_loc_dets)
        wind = view.findViewById(R.id.tv_wind_value_loc_dets)
        pressure = view.findViewById(R.id.tv_pressure_value_loc_dets)



        hourRecyclerView = view.findViewById(R.id.rv_day_hourly_state_loc_dets)
        weekRecyclerView = view.findViewById(R.id.rv_week_state_loc_dets)

        hourLinearLayoutManager = LinearLayoutManager(requireContext())
        hourLinearLayoutManager.orientation = RecyclerView.HORIZONTAL
//        hourHomeAdapter = HourHomeAdapter(requireContext())
        hourHomeAdapter = HourHomeAdapter1(requireContext())

        hourRecyclerView.adapter = hourHomeAdapter
        hourRecyclerView.layoutManager = hourLinearLayoutManager


        weekLinearLayoutManager = LinearLayoutManager(requireContext())
        weekLinearLayoutManager.orientation = RecyclerView.VERTICAL
//        weekHomeAdapter = WeekHomeAdapter(requireContext())
        weekHomeAdapter = WeekHomeAdapter1(requireContext())

        weekRecyclerView.adapter = weekHomeAdapter
        weekRecyclerView.layoutManager = weekLinearLayoutManager


        homeViewModelFactory = HomeViewModelFactory(WeatherRepositoryImp.getInstance(
            WeatherRemoteDataSourceImp.getInstance(),
                    WeatherLocalDataSourceImp.getInstance(requireContext())
        ))



        viewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        lifecycleScope.launch {
            viewModel.getAllWeathers(latit , longit , "en" , "fer" )
        }

        lifecycleScope.launch {
            viewModel._weather.collectLatest { result ->
                when(result){
                    is ApiWeather.Loading -> {

                       // recyclerView.visibility = View.GONE

                    }
                    is ApiWeather.Success -> {
                        hourRecyclerView.visibility = View.VISIBLE
                        weekRecyclerView.visibility = View.VISIBLE
                        val weather = result.data
                        city.text = weather.city.name
                       // date.text =
                        Glide.with(this@HomeFragment)
                            .load("https://openweathermap.org/img/wn/${weather.list[0].weather[0].icon}@2x.png")
                            .into(image)

                        temp.text = "${weather.list.firstOrNull()?.main?.temp.toString()}°C"
                        description.text = weather.list.firstOrNull()?.weather?.firstOrNull()?.description.toString()



                        humidity.text = "${weather.list.firstOrNull()?.main?.temp.toString()}%"
                        cloud.text = "${weather.list.firstOrNull()?.clouds?.all.toString()}%"
                        pressure.text = "${weather.list.firstOrNull()?.main?.temp.toString()}hpa"
                        wind.text = "${weather.list.firstOrNull()?.wind?.speed.toString()}m/s"

                       hourHomeAdapter.setList(weather.list)
                       weekHomeAdapter.setList(weather.list)


                        ///???????????? i should to use api to show 12 hours of the day

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
 */