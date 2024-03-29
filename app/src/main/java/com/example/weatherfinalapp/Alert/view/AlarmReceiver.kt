package com.example.weatherfinalapp.Alert.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.model.Alert

const val ALARM_RECEIVER_ACTION = "com.example.weatherfinalapp.ALARM_RECEIVER_ACTION"

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Check if the context is not null
        context?.let {
            // Retrieve the alert object from the intent
            val alert = intent?.getSerializableExtra("alert") as? Alert
            // Retrieve additional data from the intent
            val cityName = intent?.getStringExtra("cityName")
            val cityDesc = intent?.getStringExtra("cityDesc")
            val cityTemp = intent?.getStringExtra("cityTemp")
            val cityHumidity = intent?.getStringExtra("cityHumidity")
            val cityWind = intent?.getStringExtra("cityWind")
            val cityCloud = intent?.getStringExtra("cityCloud")
            val cityPressure = intent?.getStringExtra("cityPressure")

            // Check if the alert is not null
            alert?.let {
                // Create a notification to display the alarm content
                createNotification(context, cityName, cityDesc, cityTemp, cityHumidity, cityWind, cityCloud, cityPressure)
            }
        }
    }

    private fun createNotification(
        context: Context,
        cityName: String?,
        cityDesc: String?,
        cityTemp: String?,
        cityHumidity: String?,
        cityWind: String?,
        cityCloud: String?,
        cityPressure: String?
    ) {
        val channelId = "AlarmChannel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for Android Oreo and higher
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Alarm", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification content
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.alarm_icon)
            .setContentTitle(cityName)
            .setContentText("$cityDesc \n $cityCloud \n $cityHumidity \n $cityTemp \n $cityPressure \n $cityWind")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Show the notification
        notificationManager.notify(1, notificationBuilder.build())
    }
}
