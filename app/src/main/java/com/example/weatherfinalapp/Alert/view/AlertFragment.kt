package com.example.weatherfinalapp.Alert.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.alert.view.AlertViewModel
import com.example.weatherfinalapp.Alert.viewModel.AlertViewModelFactory
import com.example.weatherfinalapp.Favorite.view.FavoriteAdapter1
import com.example.weatherfinalapp.Favorite.viewModel.FavLocationViewModel
import com.example.weatherfinalapp.Favorite.viewModel.FavLocationViewModelFactory
import com.example.weatherfinalapp.Network.WeatherRemoteDataSourceImp
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.databinding.FragmentAlertBinding
import com.example.weatherfinalapp.db.WeatherLocalDataSourceImp
import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.WeatherRepositoryImp
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AlertFragment : Fragment(), OnAlertClickListener {
    private lateinit var fabAlert : FloatingActionButton
   // private lateinit var binding: FragmentAlertBinding
    private lateinit var alertViewModel: AlertViewModel
    private lateinit var factory: AlertViewModelFactory
    private lateinit var recyclerView: RecyclerView
    private lateinit var alertAdapter: AlertAdapter
    private lateinit var layoutManager: LinearLayoutManager




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_alert, container, false)
       fabAlert = view.findViewById(R.id.fab_alert)

        recyclerView = view.findViewById(R.id.rv_alert_locations_alert_list)



        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = RecyclerView.VERTICAL
        alertAdapter = AlertAdapter(requireContext() , this)
        recyclerView.adapter = alertAdapter
        recyclerView.layoutManager = layoutManager

        factory = AlertViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp.getInstance(requireContext())
            ))

        alertViewModel = ViewModelProvider(this , factory).get(AlertViewModel::class.java)

        alertViewModel.alert.observe(requireActivity()){ alert ->

            if(alert != null){
                if(alert.size == 0){
                    recyclerView.visibility = View.GONE
                }
                else
                {
                    recyclerView.visibility = View.VISIBLE
                    alertAdapter.submitList(alert)
                }
            }
        }






        fabAlert.setOnClickListener{
          startActivity(Intent(requireContext(), DialogAlertActivity::class.java))

        }




        return view
    }

    override fun deleteAlert(alert: Alert) {
        alertViewModel.removeAlertFromList(alert)
    }




}