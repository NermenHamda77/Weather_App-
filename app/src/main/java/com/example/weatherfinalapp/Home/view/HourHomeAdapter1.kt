package com.example.weatherfinalapp.Home.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.model.ListWeather
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.HashSet
import java.util.Locale


class HourHomeAdapter1(private val context: Context) : RecyclerView.Adapter<HourHomeAdapter1.ViewHolder>() {
    private var listOfTodayWeather = listOf<ListWeather>()

    fun setList(newList: List<ListWeather>){
        val uniqueDate = HashSet<String>()

        val filterList = newList.filter { entry ->
            val dateParts = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(entry.dt_txt)
                ?.let {
                    SimpleDateFormat(
                        "hh:mm a",
                        Locale.getDefault()
                    ).format(it)
                }
            if(uniqueDate.contains(dateParts)){
                false
            }else{
                if (dateParts != null) {
                    uniqueDate.add(dateParts)
                }
                true
            }
        }
        listOfTodayWeather = filterList
        notifyDataSetChanged()
    }

   /* fun setList(newList: List<ListWeather>){
        listOfTodayWeather = newList
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.hourly_weather_card,parent,false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listOfTodayWeather.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todayForeCast = listOfTodayWeather[position]
        val temperatureFahrenheit = todayForeCast.main.temp
        val temperatureCelsuis = (temperatureFahrenheit.minus(273.15))
        val temperatureFormatted = String.format("%.2f" , temperatureCelsuis)

        if (todayForeCast.weather.isNotEmpty()) {
            val weather = todayForeCast.weather[0] // Assuming you're only interested in the first weather condition
            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${weather.icon}.png")
                .into(holder.image)
        }

        holder.temp.text = "$temperatureFormatted °c"


        holder.time.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(todayForeCast.dt_txt)
            ?.let {
                SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                ).format(it)
            }

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_hourly_card)
        val time: TextView = itemView.findViewById(R.id.tv_time_hourly_card)
        val temp: TextView = itemView.findViewById(R.id.tv_temp_hourly_card)

    }




}

/*
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.forecastApp.WeatherList
import com.example.weatherapp.R
import java.text.SimpleDateFormat
import java.util.Calendar

class WeatherTodayAdapter : RecyclerView.Adapter<TodayHolder>() {
    private var listOfTodayWeather = listOf<WeatherList>()

    fun setList(listOfToday : List<WeatherList>){
        this.listOfTodayWeather = listOfToday
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.today_hourly_list,parent,false)
        return  TodayHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfTodayWeather.size
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TodayHolder, position: Int) {
        val todayForeCast = listOfTodayWeather[position]
        holder.timeDisplay.text = todayForeCast.dtTxt!!.substring(11,16).toString()
        val temperatureFahrenheit = todayForeCast.main?.temp
        val temperatureCelsuis = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f" , temperatureCelsuis)

        holder.tempDisplay.text = "$temperatureFormatted °c"

        val calendar : Calendar = Calendar.getInstance()

        // Define the desired format
        val dateFormat = SimpleDateFormat("HH::mm")
        val formattedTime = dateFormat.format(calendar.time)

        val timeOfApi = todayForeCast.dtTxt!!.split(" ")
        val partAfterSpace = timeOfApi[1]

        Log.e("time" , "formatted time: ${formattedTime}, timeOfApi: ${partAfterSpace}")


}

class TodayHolder(view: View) : RecyclerView.ViewHolder(view){
    val timeDisplay: TextView =view.findViewById(R.id.timeDisplay)
    val tempDisplay: TextView =view.findViewById(R.id.tempDisplay)
    val imageDisplay: ImageView =view.findViewById(R.id.imageDisplay)
}


 */