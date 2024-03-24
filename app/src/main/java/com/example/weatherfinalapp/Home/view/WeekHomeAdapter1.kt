package com.example.weatherfinalapp.Home.view

import android.content.Context
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
import java.util.Locale

class WeekHomeAdapter1(private val context: Context) : RecyclerView.Adapter<WeekHomeAdapter1.ViewHolder>() {
        private var listOfTodayWeather = listOf<ListWeather>()

        fun setList(newList: List<ListWeather>){
            val uniqueDate = HashSet<String>()

            val filterList = newList.filter { entry ->
                val dateParts = entry.dt_txt.split(" ")[0]
                if(uniqueDate.contains(dateParts)){
                    false
                }else{
                    uniqueDate.add(dateParts)
                    true
                }
            }
            listOfTodayWeather = filterList
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return listOfTodayWeather.size
    }

    //class WeatherTodayAdapter : RecyclerView.Adapter<TodayHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekHomeAdapter1.ViewHolder {

        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.week_weather_card,parent,false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekHomeAdapter1.ViewHolder, position: Int) {
        val foreCastObject = listOfTodayWeather[position]

        for (i in foreCastObject.weather){
            holder.description.text = i.description!!
        }


      //  holder.humidity.text = foreCastObject.main!!.humidity.toString() //
        //holder.windSpeed.text = foreCastObject.wind!!.speed.toString() //

        if (foreCastObject.weather.isNotEmpty()) {
            val weather = foreCastObject.weather[0] // Assuming you're only interested in the first weather condition
            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${weather.icon}.png")
                .into(holder.image)
        }

        val temperatureFahrenheit = foreCastObject.main?.temp
        val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f" , temperatureCelsius)

        holder.temp.text = "$temperatureFormatted °c"

        // val calendar : Calendar = Calendar.getInstance()

        // Define the desired format
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm" , Locale.getDefault())
        val date = inputFormat.parse(foreCastObject.dt_txt!!)
        val outputFormat = SimpleDateFormat("d MMMM EEEE" , Locale.getDefault())
        val dateAndDayName = outputFormat.format(date!!)

        holder.day.text = dateAndDayName




    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_weather_week_card)
        val day: TextView = itemView.findViewById(R.id.tv_day_name_week_card)
        val description: TextView = itemView.findViewById(R.id.tv_description_week_card)
        val temp: TextView = itemView.findViewById(R.id.tv_temp_state_week_card)

    }





}

/*

class HourHomeAdapter1(private val context: Context) : RecyclerView.Adapter<HourHomeAdapter1.ViewHolder>() {
    private var listOfTodayWeather = listOf<ListWeather>()

    fun setList(newList: List<ListWeather>){
        val uniqueDate = HashSet<String>()

        val filterList = newList.filter { entry ->
            val dateParts = entry.dt_txt.split(" ")[0]
            if(uniqueDate.contains(dateParts)){
                false
            }else{
                uniqueDate.add(dateParts)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.hourly_weather_card,parent,false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listOfTodayWeather.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = listOfTodayWeather[position]

        Glide.with(context).load("https://openweathermap.org/img/wn/${weather.weather[0].icon}.png")
            .into(holder.image)
        holder.temp.text =  "${listOfTodayWeather.get(position).main.temp.toString()}°C"

        val currentTime = Calendar.getInstance().time

        // Define the time format
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Format the current time
        val formattedTime = timeFormat.format(currentTime)

        // Set the formatted time to the TextView
        holder.time.text = formattedTime
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_hourly_card)
        val time: TextView = itemView.findViewById(R.id.tv_time_hourly_card)
        val temp: TextView = itemView.findViewById(R.id.tv_temp_hourly_card)

    }




}

 */