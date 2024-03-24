package com.example.weatherfinalapp.Home.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.Home.viewModel.HomeViewModel
import com.example.weatherfinalapp.Home.viewModel.HomeViewModelFactory
import com.example.weatherfinalapp.LocationDetailsScreen.viewModel.LocationDetailsViewModel
import com.example.weatherfinalapp.LocationDetailsScreen.viewModel.LocationDetailsViewModelFactory
import com.example.weatherfinalapp.Network.WeatherRemoteDataSourceImp
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.db.WeatherLocalDataSourceImp
import com.example.weatherfinalapp.model.ApiWeather
import com.example.weatherfinalapp.model.WeatherRepositoryImp
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

                    }
                    else ->
                    {
                        Toast.makeText(requireContext(),"Error", Toast.LENGTH_SHORT).show()

                        Log.i(TAG, "onCreateView: error")
                     //   Toast.makeText(this@AllProductActivity,"product", Toast.LENGTH_SHORT).show()

                    }

                }
            }

        }
        geocoder = Geocoder(requireContext(), Locale.getDefault())

        var fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        var locationRequest: LocationRequest = LocationRequest.Builder(1000).apply {
            setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        }.build()
        var callback: LocationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                val location = p0.lastLocation
                latit = location?.latitude ?: 0.0
                longit = location?.longitude ?: 0.0
                Log.i(TAG, "onLocationResult:lat $latit  and longitude $longit ")

                // Call viewModel.getAllWeathers() with updated latit and longit
                lifecycleScope.launch {
                    viewModel.getAllWeathers(latit, longit, "en", "fer")
                }
            }
        }
        /*

         */

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf( Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),locationRequestID)

        }
        fusedClient.requestLocationUpdates(locationRequest,callback, Looper.myLooper(),)


        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){}else{
            var fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            var locationRequest:LocationRequest = LocationRequest.Builder(1000).apply {
                setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
            }.build()
            var callback:LocationCallback = object :LocationCallback(){
                override fun onLocationResult(p0: LocationResult) {
                }
            }
        }
    }

}

/*
 private lateinit var viewModel: ViewModelProduct
    private lateinit var adapter: MyAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_activity2_products)


        val productDao = ProductDatabase.getInstance(applicationContext).getProductDao()
        val localDataSource = LocalDataSource(productDao)
        val remoteDataSource = RemoteDataSource(API.retrofitService)
        val productRepo = ProductRepoImp(localDataSource, remoteDataSource)
        val viewModelFactory = ViewModelProductFactory(productRepo)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ViewModelProduct::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.RC)
        adapter = MyAdapter(emptyList(),this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.productList.observe(this, { productList ->
            adapter.updateData(productList)
        })

        viewModel.fetchProducts()

    }

    override fun add(product: Product) {
        viewModel.addToFavorites(product)
    }

    override fun remove(product: Product) {

    }
 */