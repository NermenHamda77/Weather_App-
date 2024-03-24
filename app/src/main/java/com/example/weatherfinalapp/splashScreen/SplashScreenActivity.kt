package com.example.weatherfinalapp.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherfinalapp.BottomNavigation.BottomNavigationActivity
import com.example.weatherfinalapp.R


class SplashScreenActivity : AppCompatActivity() {
    private lateinit var appName: TextView
    private lateinit var lottie: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        appName = findViewById(R.id.appname)
        lottie = findViewById(R.id.lottie)

        appName.animate().translationY(-1500f).setDuration(2700).setStartDelay(0)
        lottie.animate().translationX(2000f).setDuration(2000).setStartDelay(2900)

        Handler().postDelayed({

            val intent = Intent(applicationContext, BottomNavigationActivity::class.java)

            startActivity(intent)
            finish()
        }, 5000)
    }
}