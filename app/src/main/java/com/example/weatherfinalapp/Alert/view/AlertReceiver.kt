package com.example.weatherfinalapp.Alert.view

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherfinalapp.R

private const val TAG = "AlertReceiver"
const val ALERT_RECEIVER_ACTION = "com.example.weatherfinalapp.ALERT_RECEIVER_ACTION"
class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Check if context is not null
        if (context == null) {
            Log.e(TAG, "Context is null")
            return
        }

        val notificationIntent = Intent(context, AlertFragment::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE)

        val cityName = intent?.getStringExtra("cityName")
        val cityDesc = intent?.getStringExtra("cityDesc")
        val cityTemp = intent?.getStringExtra("cityTemp")
        val cityHumidity = intent?.getStringExtra("cityHumidity")
        val cityWind = intent?.getStringExtra("cityWind")
        val cityCloud = intent?.getStringExtra("cityCloud")
        val cityPresure = intent?.getStringExtra("cityPressure")
        Log.i(TAG, "onCreate: $cityName $cityDesc")

        // Use BigTextStyle to make the notification expandable
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText("$cityDesc \n $cityCloud \n $cityHumidity \n $cityTemp \n $cityPresure \n $cityWind")

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "channelId")
            .setSmallIcon(R.drawable.alert_icon_2)
            .setContentTitle(cityName)
            .setContentText(cityDesc)
            .setStyle(bigTextStyle) // Apply BigTextStyle to the notification
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent) // Set the intent that will fire when the user taps the notification

        val notificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "Permissions needed", Toast.LENGTH_SHORT).show()
            return
        }
        notificationManagerCompat.notify(123, builder.build())
        Toast.makeText(context, "No permissions needed", Toast.LENGTH_SHORT).show()
    }
}
