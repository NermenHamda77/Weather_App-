package com.example.weatherfinalapp.Home.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.Home.viewModel.HomeViewModel
import com.example.weatherfinalapp.Home.viewModel.HomeViewModelFactory
import com.example.weatherfinalapp.Network.WeatherRemoteDataSourceImp
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.Settings.view.SharedPreferencesManager
import com.example.weatherfinalapp.db.WeatherLocalDataSourceImp
import com.example.weatherfinalapp.model.ApiWeather
import com.example.weatherfinalapp.model.WeatherRepositoryImp
import com.example.weatherfinalapp.model.WeatherResponse
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
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
    private lateinit var hourHomeAdapter: HourHomeAdapter1
    private lateinit var weekHomeAdapter: WeekHomeAdapter1

    private lateinit var hourRecyclerView: RecyclerView
    private lateinit var weekRecyclerView: RecyclerView

    private lateinit var hourLinearLayoutManager: LinearLayoutManager
    private lateinit var weekLinearLayoutManager: LinearLayoutManager

    private val locationRequestID = 1
    private var latit :  Double = 0.0
    private var longit : Double = 0.0

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        city = view.findViewById(R.id.tv_city_name)
        date = view.findViewById(R.id.tv_date_top_home)
        description = view.findViewById(R.id.tv_city_state)
        temp = view.findViewById(R.id.tv_city_temp)
        image = view.findViewById(R.id.iv_city_state)
        cloud = view.findViewById(R.id.tv_cloud_value)
        humidity = view.findViewById(R.id.tv_humidity_value)
        wind = view.findViewById(R.id.tv_wind_value)
        pressure = view.findViewById(R.id.tv_pressure_value)

        hourRecyclerView = view.findViewById(R.id.rv_day_hourly_state)
        weekRecyclerView = view.findViewById(R.id.rv_week_state)

        hourLinearLayoutManager = LinearLayoutManager(requireContext())
        hourLinearLayoutManager.orientation = RecyclerView.HORIZONTAL
        hourHomeAdapter = HourHomeAdapter1(requireContext())

        hourRecyclerView.adapter = hourHomeAdapter
        hourRecyclerView.layoutManager = hourLinearLayoutManager

        weekLinearLayoutManager = LinearLayoutManager(requireContext())
        weekLinearLayoutManager.orientation = RecyclerView.VERTICAL
        weekHomeAdapter = WeekHomeAdapter1(requireContext())

        weekRecyclerView.adapter = weekHomeAdapter
        weekRecyclerView.layoutManager = weekLinearLayoutManager

        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        homeViewModelFactory = HomeViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp.getInstance(requireContext())
            ),
            sharedPreferencesManager)

        viewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        observeWeatherData()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                val location = p0.lastLocation
                latit = location?.latitude ?: 0.0
                longit = location?.longitude ?: 0.0
                lifecycleScope.launch {
                    viewModel.getAllWeathers(latit, longit)
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),locationRequestID)

        } else {
            requestLocationUpdates()
        }

        return view
    }

    private fun observeWeatherData() {
        lifecycleScope.launch {
            viewModel._weather.collectLatest { result ->
                when (result) {
                    is ApiWeather.Loading -> {
                        // Handle loading state
                    }
                    is ApiWeather.Success -> {
                        val weather = result.data
                        updateWeatherUI(weather)
                    }
                    is ApiWeather.Failure -> {
                        Toast.makeText(requireContext(), "Error fetching weather data", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Error fetching weather data: ${result.msg}")
                    }
                }
            }
        }
    }

    private fun updateWeatherUI(weather: WeatherResponse) {
        city.text = weather.city.name
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${weather.list.firstOrNull()?.weather?.firstOrNull()?.icon}@2x.png")
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
        hourHomeAdapter.setList(weather.list)
        weekHomeAdapter.setList(weather.list)
        updateWindUI(weather.list.firstOrNull()?.wind?.speed ?: 0.0)
    }



    private fun updateWindUI(speed: Double) {
        val windValueTextView = view?.findViewById<TextView>(R.id.tv_wind_value)
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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestLocationUpdates() {
        val locationRequest: LocationRequest = LocationRequest.Builder(1000)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .build()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
