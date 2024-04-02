package com.example.weatherfinalapp.Alert.view
import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.weatherfinalapp.R

private const val TAG = "NotificationReceiver"

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) {
            Log.e(TAG, "Context is null")
            return
        }


        val cityName = intent?.getStringExtra("cityName")
        val cityDesc = intent?.getStringExtra("cityDesc")
        val cityTemp = intent?.getStringExtra("cityTemp")
        val cityHumidity = intent?.getStringExtra("cityHumidity")
        val cityWind = intent?.getStringExtra("cityWind")
        val cityCloud = intent?.getStringExtra("cityCloud")
        val cityPresure = intent?.getStringExtra("cityPressure")

        val notificationIntent = Intent(context, AlertFragment::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val soundUri: Uri = Uri.parse("android.resource://${context.packageName}/${R.raw.alarm_sound}")

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText("$cityDesc \n $cityCloud \n $cityHumidity \n $cityTemp \n $cityPresure \n $cityWind")

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "channelId")
            .setSmallIcon(R.drawable.alarm_icon)
            .setContentTitle(cityName)
            .setContentText(cityDesc)
            .setStyle(bigTextStyle)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setSound(soundUri)

        // Notify
        val notificationManagerCompat: NotificationManagerCompat = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManagerCompat.notify(123, builder.build())
    }
}
