package com.example.weatherfinalapp.BottomNavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weatherfinalapp.Alert.view.AlertFragment
import com.example.weatherfinalapp.Favorite.view.FavoriteFragment
import com.example.weatherfinalapp.Home.view.HomeFragment
import com.example.weatherfinalapp.Map.view.MapFragment
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.Settings.view.SettingsFragment
import com.example.weatherfinalapp.databinding.ActivityBottomNavigationBinding


class BottomNavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        replaceFragments(HomeFragment())
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_id -> replaceFragments(HomeFragment())
                R.id.alert_id -> replaceFragments(AlertFragment())
                R.id.settings_id -> replaceFragments(SettingsFragment())
                R.id.fav_id -> replaceFragments(FavoriteFragment())
            }
            true
        }
    }

    private fun replaceFragments(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
