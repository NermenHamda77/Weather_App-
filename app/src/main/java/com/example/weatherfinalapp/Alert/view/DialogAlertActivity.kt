package com.example.weatherfinalapp.Alert.view

import com.example.weatherapp.alert.view.AlertViewModel
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherfinalapp.Alert.viewModel.AlertViewModelFactory
import com.example.weatherfinalapp.Map.view.MapActivity
import com.example.weatherfinalapp.Network.WeatherRemoteDataSourceImp
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.db.WeatherLocalDataSourceImp
import com.example.weatherfinalapp.model.Alert
import com.example.weatherfinalapp.model.ApiWeather
import com.example.weatherfinalapp.model.WeatherRepositoryImp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar

class DialogAlertActivity : AppCompatActivity() {
    // Static variable to keep track of the last used ID
    companion object {
        private var uniqueIdCounter = 0
    }

    private lateinit var mapImage: ImageView
    private lateinit var locationTitle: TextView
    private lateinit var fromValue: TextView
    private lateinit var toValue: TextView
    private lateinit var dateValue: TextView

    private lateinit var fromImage: ImageView
    private lateinit var toImage: ImageView
    private lateinit var calendareImage: ImageView

    private lateinit var saveBtn : Button
    private lateinit var choosenType : String
    private lateinit var notification : RadioButton
    private lateinit var alarm : RadioButton
    private lateinit var group : RadioGroup

    private lateinit var dialogViewModel: AlertViewModel
    private lateinit var factory: AlertViewModelFactory

    private var cityName : String = "city"
    private var cityDescription : String = "desc"
    private var cityTemp : String = "temp"
    private var cityWind : String = "wind"
    private var cityHumidity : String = "humidity"
    private var cityPressure : String = "pressure"
    private var cityCloud : String = "cloud"

    private var value : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_alert)

        locationTitle = findViewById(R.id.tv_alert_location_value)
        fromValue = findViewById(R.id.tv_from_value)
        toValue = findViewById(R.id.alert_to_txt)
        dateValue = findViewById(R.id.tv_alert_date_value)

        fromImage = findViewById(R.id.iv_from_time_btn)
        toImage = findViewById(R.id.iv_to_time_btn)
        mapImage = findViewById(R.id.iv_map_btn)
        calendareImage = findViewById(R.id.calender_btn)
        saveBtn = findViewById(R.id.save_alert_btn)

        notification = findViewById(R.id.radio_notification)
        alarm =  findViewById(R.id.radio_alarm)
        group = findViewById(R.id.alert_radio_group)

        createNotificationChannel()

        factory = AlertViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp.getInstance(this)
            ))

        dialogViewModel = ViewModelProvider(this, factory).get(AlertViewModel::class.java)

        fromImage.setOnClickListener {
            value = 1
            showTimePicker()
        }

        toImage.setOnClickListener {
            value = 2
            showTimePicker()
        }

        calendareImage.setOnClickListener {
            showDatePicker()
        }

        mapImage.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("PLACE" , "ALERT")
            startActivity(intent)
        }

        group.setOnCheckedChangeListener { _ ,chosen ->
            val type = findViewById<View>(chosen) as RadioButton
            when(type){
                notification -> {
                    choosenType = "notification"
                }
                alarm -> {
                    choosenType = "alarm"
                }
            }
        }

        val address = intent.getStringExtra("address")
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lon = intent.getDoubleExtra("lon", 0.0)
        locationTitle.text = address.toString()

        saveBtn.setOnClickListener{
            val alert = Alert(
                date = dateValue.text.toString(),
                fromTime = fromValue.text.toString(),
                toTime = toValue.text.toString(),
                alertType = choosenType,
                address = address,
                latitude = lat,
                longitude = lon
            )
            dialogViewModel.addAlertToList(alert)
            Toast.makeText(this,"done", Toast.LENGTH_SHORT).show()
            lifecycleScope.launch {
                dialogViewModel.getAllWeathersOfLoc(lat , lon , "en" , "fer" )
            }

            lifecycleScope.launch {
                dialogViewModel._weather.collectLatest { result ->
                    when(result){
                        is ApiWeather.Loading -> {
                            // recyclerView.visibility = View.GONE
                        }
                        is ApiWeather.Success -> {
                            val weather = result.data
                            cityName = weather.city.name
                            cityDescription = weather.list.firstOrNull()?.weather?.firstOrNull()?.description.toString()
                            cityTemp = "${weather.list.firstOrNull()?.main?.temp.toString()}°C"



                            cityHumidity = "${weather.list.firstOrNull()?.main?.humidity.toString()}%"
                            cityCloud = "${weather.list.firstOrNull()?.clouds?.all.toString()}%"
                            cityPressure = "${weather.list.firstOrNull()?.main?.pressure.toString()}hpa"
                            cityWind = "${weather.list.firstOrNull()?.wind?.speed.toString()}m/s"


                            when(choosenType){
                                "notification" -> {
                                    val uniqueId = generateUniqueId()
                                    scheduleNotification(alert, uniqueId)
                                }
                                "alarm" -> {
                                   /* val uniqueId = generateUniqueId()
                                    scheduleAlarm(alert, uniqueId)*/
                                }
                            }




                        }
                        else -> {
                            // Handle other cases if necessary
                        }
                    }
                }
            }
            finish()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channelIdRemainderChannel"
            val description = "Channel for alarm manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channelId = "channelId"

            val channel = NotificationChannel(channelId, name, importance).apply {
                setDescription(description)
            }

            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                if(value == 1){
                    fromValue.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                }
                else if(value == 2){
                    toValue.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                }
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                dateValue.text = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun generateUniqueId(): Int {
        return uniqueIdCounter++
    }

    private fun scheduleNotification(alert: Alert, uniqueId: Int) {
        val intent = Intent(this, AlertReceiver::class.java)
        intent.putExtra("alert", alert)
        intent.putExtra("cityName" , cityName)
        intent.putExtra("cityDesc" , cityDescription)
        intent.putExtra("cityPressure" , cityPressure)
        intent.putExtra("cityHumidity" , cityHumidity)
        intent.putExtra("cityWind" , cityWind)
        intent.putExtra("cityCloud" , cityCloud)
        intent.putExtra("cityTemp" , cityTemp)


        val pendingIntent = PendingIntent.getBroadcast(
            this,
            uniqueId, // Use the unique ID here
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val currentTimeMillis = System.currentTimeMillis()
        val fromTimeMillis = parseTimeToMillis(alert.fromTime)
        //val toTimeMillis = parseTimeToMillis(alert.toTime)

        val delay = fromTimeMillis - currentTimeMillis
       // val duration = toTimeMillis - fromTimeMillis

        try {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + delay,
                pendingIntent
            )
        } catch (e: SecurityException) {
            // Handle the SecurityException by falling back to inexact alarms
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + delay,
                pendingIntent
            )
        }
    }

    private fun parseTimeToMillis(time: String): Long {
        val parts = time.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

  /*  private fun scheduleAlarm(alert: Alert, uniqueId: Int) {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("alert", alert)
        intent.putExtra("cityName" , cityName)
        intent.putExtra("cityDesc" , cityDescription)
        intent.putExtra("cityPressure" , cityPressure)
        intent.putExtra("cityHumidity" , cityHumidity)
        intent.putExtra("cityWind" , cityWind)
        intent.putExtra("cityCloud" , cityCloud)
        intent.putExtra("cityTemp" , cityTemp)
        intent.putExtra("choosenType" , choosenType) // Pass the chosen type to the receiver

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            uniqueId, // Use the unique ID here
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val currentTimeMillis = System.currentTimeMillis()
        val fromTimeMillis = parseTimeToMillis(alert.fromTime)

        val delay = fromTimeMillis - currentTimeMillis

        try {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + delay,
                pendingIntent
            )
        } catch (e: SecurityException) {
            // Handle the SecurityException by falling back to inexact alarms
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + delay,
                pendingIntent
            )
        }
        // Play the alarm sound
        playAlarmSound(this)
    }

    private fun playAlarmSound(context: Context) {
        try {
            // Create a MediaPlayer instance to play the alarm sound
            val mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound)
            // Start playing the sound
            mediaPlayer.start()

            // Add an event listener to release resources when the sound finishes playing
            mediaPlayer.setOnCompletionListener { mp ->
                // Release the MediaPlayer resources
                mp.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/
}



