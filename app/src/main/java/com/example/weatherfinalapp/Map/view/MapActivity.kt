package com.example.weatherfinalapp.Map.view

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Rect
import android.location.Geocoder
import android.location.GpsStatus
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.weatherfinalapp.Alert.view.DialogAlertActivity

import com.example.weatherfinalapp.Map.viewModel.MapViewModel
import com.example.weatherfinalapp.Map.viewModel.MapViewModelFactory
import com.example.weatherfinalapp.Network.WeatherRemoteDataSourceImp
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.databinding.ActivityMapBinding
import com.example.weatherfinalapp.db.WeatherLocalDataSourceImp
import com.example.weatherfinalapp.model.FavoriteLocation
import com.example.weatherfinalapp.model.WeatherRepositoryImp

import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.Locale

class MapActivity : AppCompatActivity(), MapListener, GpsStatus.Listener {


    private lateinit var pinMarker : Marker

    var latit :  Double = 0.0
    var longit : Double = 0.0


    private lateinit var placeValue : String

    lateinit var mMap: MapView
    lateinit var controller: IMapController
    lateinit var mMyLocationOverlay: MyLocationNewOverlay
    lateinit var binding : ActivityMapBinding
    lateinit var geocoder: Geocoder
    private lateinit var factory: MapViewModelFactory
    private lateinit var viewModel: MapViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Configuration.getInstance().load(
            applicationContext,
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        )
        mMap = binding.osmmap
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.mapCenter
        mMap.setMultiTouchControls(true)
        mMap.getLocalVisibleRect(Rect())




        mMyLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), mMap)
        controller = mMap.controller

        mMyLocationOverlay.enableMyLocation()
        mMyLocationOverlay.enableFollowLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true
        mMyLocationOverlay.runOnFirstFix {
            runOnUiThread {
                controller.setCenter(mMyLocationOverlay.myLocation);
                controller.animateTo(mMyLocationOverlay.myLocation)
            }
        }
        // val mapPoint = GeoPoint(latitude, longitude)

        controller.setZoom(6.0)

        Log.e("TAG", "onCreate:in ${controller.zoomIn()}")
        Log.e("TAG", "onCreate: out  ${controller.zoomOut()}")

        // controller.animateTo(mapPoint)
        mMap.overlays.add(mMyLocationOverlay)

        mMap.addMapListener(this)

        pinMarker = Marker(mMap)
        binding.osmmap.overlays.add(TapOverlay())


        geocoder = Geocoder(this, Locale.getDefault())

        factory = MapViewModelFactory(
            WeatherRepositoryImp.getInstance(
            WeatherRemoteDataSourceImp.getInstance(),
            WeatherLocalDataSourceImp.getInstance(this)
        ))



        viewModel = ViewModelProvider(this, factory).get(MapViewModel::class.java)


        placeValue = intent.getStringExtra("PLACE").toString()   // FAVORITE OR ALERT

    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        // event?.source?.getMapCenter()
        Log.e("TAG", "onCreate:la ${event?.source?.getMapCenter()?.latitude}")
        Log.e("TAG", "onCreate:lo ${event?.source?.getMapCenter()?.longitude}")
        //  Log.e("TAG", "onScroll   x: ${event?.x}  y: ${event?.y}", )
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        //  event?.zoomLevel?.let { controller.setZoom(it) }


        Log.e("TAG", "onZoom zoom level: ${event?.zoomLevel}   source:  ${event?.source}")
        return false;
    }

    override fun onGpsStatusChanged(event: Int) {


        TODO("Not yet implemented")
    }

    private inner class TapOverlay : Overlay() {
        override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
            val point = mapView?.projection?.fromPixels(e?.x?.toInt() ?: 0, e?.y?.toInt() ?: 0)
            binding.osmmap.overlays.remove(pinMarker)
            pinMarker = Marker(mapView)
            pinMarker.position = point as GeoPoint
            binding.osmmap.overlays.add(pinMarker)
            binding.osmmap.invalidate()

            latit = point.latitude
            longit = point.longitude

            val address = getAddress(latit, longit)

            val favoriteLocation = FavoriteLocation(latit, longit, address)

            when (placeValue) {
                "ALERT" -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@MapActivity)
                    builder.setTitle("Do You Want To Add ${address} To Your Alert?")
                        .setPositiveButton("Add") { dialog, which ->
                            if (address != null){
                              //  viewModel.addLocationToFav(favoriteLocation)
                                val intent = Intent(this@MapActivity, DialogAlertActivity::class.java)
                                intent.putExtra("address" , address)
                                intent.putExtra("lat" , latit)
                                intent.putExtra("lon" , longit)
                                startActivity(intent)
                                finish()
                                Toast.makeText(
                                    this@MapActivity,
                                    "$address is added to Alert",
                                    Toast.LENGTH_SHORT
                                ).show()


                            }else
                            {
                                Toast.makeText(this@MapActivity , "Fail in adding this Location to Alert" , Toast.LENGTH_SHORT).show()

                            }
                            dialog.dismiss()

                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .show()

                }
                "FAVORITE" -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this@MapActivity)
                    builder.setTitle("Do You Want To Add ${address} To Your Favorite Locations?")
                        .setPositiveButton("Add") { dialog, which ->
                            if (address != null){
                                viewModel.addLocationToFav(favoriteLocation)
                                Toast.makeText(
                                    this@MapActivity,
                                    "$address is added to Favorite",
                                    Toast.LENGTH_SHORT
                                ).show()
                               // finish()
                            }else
                            {
                                Toast.makeText(this@MapActivity , "Fail in adding this Location to Fav" , Toast.LENGTH_SHORT).show()

                            }
                            dialog.dismiss()

                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setCancelable(false)
                        .show()

                }
                else -> {
                    // Handle other cases if necessary
                }
            }
            // Now you can use favoriteLocation as needed






        return true
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String? {
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
    }




}