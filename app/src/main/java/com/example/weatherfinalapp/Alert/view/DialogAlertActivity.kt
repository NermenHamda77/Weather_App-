package com.example.weatherfinalapp.Alert.view
import android.app.*
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.alert.view.AlertViewModel
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
import java.text.SimpleDateFormat
import java.util.*


class DialogAlertActivity : AppCompatActivity() {

    companion object {
        private var uniqueIdCounter = 0
        const val CHANNEL_ID = "channelId"
    }

    private lateinit var mapImage: ImageView
    private lateinit var locationTitle: TextView
    private lateinit var timeValue: TextView
    private lateinit var dateValue: TextView

    private lateinit var timeImage: ImageView
    private lateinit var calendareImage: ImageView

    private lateinit var saveBtn : Button
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog_alert)

        locationTitle = findViewById(R.id.tv_alert_location_value)
        timeValue = findViewById(R.id.tv_time_value)
        dateValue = findViewById(R.id.tv_alert_date_value)

        timeImage = findViewById(R.id.iv_time_btn)
        mapImage = findViewById(R.id.iv_map_btn)
        calendareImage = findViewById(R.id.calender_btn)
        saveBtn = findViewById(R.id.save_alert_btn)

        notification = findViewById(R.id.radio_notification)
        alarm =  findViewById(R.id.radio_alarm)
        group = findViewById(R.id.alert_radio_group)

        factory = AlertViewModelFactory(
            WeatherRepositoryImp.getInstance(
                WeatherRemoteDataSourceImp.getInstance(),
                WeatherLocalDataSourceImp.getInstance(this)
            ))

        dialogViewModel = ViewModelProvider(this, factory).get(AlertViewModel::class.java)

        timeImage.setOnClickListener {
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
            val type = findViewById<RadioButton>(chosen)
            when(type){
                notification -> {
                    // Notification radio button clicked
                }
                alarm -> {
                    // Alarm radio button clicked
                }
            }
        }

        val address = intent.getStringExtra("address")
        val lat = intent.getDoubleExtra("lat", 0.0)
        val lon = intent.getDoubleExtra("lon", 0.0)
        locationTitle.text = address.toString()

        saveBtn.setOnClickListener{
            val chosenDate = dateValue.text.toString()
            val chosenTime = timeValue.text.toString()

            val alert = Alert(
                date = chosenDate,
                time = chosenTime,
                alertType = "notification",
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
                            // Handle loading state
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

                            // Schedule notification
                            val uniqueId = generateUniqueId()
                            scheduleNotification(alert, uniqueId, chosenDate, chosenTime)
                        }
                        else -> {
                            // Handle other cases if necessary
                        }
                    }
                }
            }
            finish()
        }

        // Call createNotificationChannel method
        createNotificationChannel()
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

    private fun generateUniqueId(): Int {
        return uniqueIdCounter++
    }

    private fun scheduleNotification(alert: Alert, uniqueId: Int, chosenDate: String, chosenTime: String) {
        val intent = Intent(this, NotificationReceiver::class.java).apply {
            putExtra("alert", alert)
            putExtra("cityName", cityName)
            putExtra("cityDesc", cityDescription)
            putExtra("cityPressure", cityPressure)
            putExtra("cityHumidity", cityHumidity)
            putExtra("cityWind", cityWind)
            putExtra("cityCloud", cityCloud)
            putExtra("cityTemp", cityTemp)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            uniqueId,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        // Calculate notification time
        val notificationTimeMillis = calculateNotificationTime(chosenDate, chosenTime)

        // Calculate delay
        val delay = notificationTimeMillis - System.currentTimeMillis()

        if (delay > 0) {
            try {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    notificationTimeMillis,
                    pendingIntent
                )
            } catch (e: SecurityException) {
                // Handle SecurityException
            }
        } else {
            // The specified time has already passed
            // Handle it accordingly
        }
    }

    private fun showTimePicker() {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val is24HourFormat = false // Set to false for 12-hour format

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val hourOfDay = if (selectedHour > 12) selectedHour - 12 else selectedHour
                val amPm = if (selectedHour >= 12) "PM" else "AM"

                timeValue.text = String.format("%02d:%02d %s", hourOfDay, selectedMinute, amPm)

            },
            hour,
            minute,
            is24HourFormat
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

    private fun calculateNotificationTime(date: String, time: String): Long {
        val dateTimeString = "$date $time"
        val formatter = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        val dateTime = formatter.parse(dateTimeString)
        return dateTime?.time ?: 0
    }
}
