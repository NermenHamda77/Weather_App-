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
import com.example.weatherfinalapp.model.WeatherResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WeekHomeAdapter(private val context: Context) :
        ListAdapter<ListWeather, WeekHomeAdapter.ViewHolder>(WeekHomeAdapter.WeatherDiffCallback())  {

//class WeatherTodayAdapter : RecyclerView.Adapter<TodayHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekHomeAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.week_weather_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekHomeAdapter.ViewHolder, position: Int) {
        val weather = getItem(position)

        // Check if the weather data and its list are not null and have elements
        if (weather != null && weather.weather.isNotEmpty()) {
            holder.temp.text = weather.main.temp.toString()

            // Ensure the position is within the bounds of the list
            if (position < weather.weather.size) {
                holder.description.text = weather.weather[position].description
                // Assuming the date should be the same for all items, you can move it outside the if condition
                val currentDate = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(currentDate)
                holder.day.text = formattedDate

                // Load image using Glide
                Glide.with(context)
                    .load("https://openweathermap.org/img/wn/${weather.weather[position].icon}.png")
                    .into(holder.image)
            }
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_weather_week_card)
        val day: TextView = itemView.findViewById(R.id.tv_day_name_week_card)
        val description: TextView = itemView.findViewById(R.id.tv_description_week_card)
        val temp: TextView = itemView.findViewById(R.id.tv_temp_state_week_card)

    }



    class WeatherDiffCallback : DiffUtil.ItemCallback<ListWeather>() {
        override fun areItemsTheSame(oldItem: ListWeather, newItem: ListWeather): Boolean {
            return oldItem.weather.get(0).id == newItem.weather.get(0).id             ///??????
        }

        override fun areContentsTheSame(
            oldItem: ListWeather,
            newItem: ListWeather
        ): Boolean {
            return oldItem == newItem
        }
    }


}
/*
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
}

class TodayHolder(view: View) : RecyclerView.ViewHolder(view){
    val timeDisplay: TextView =view.findViewById(R.id.timeDisplay)
    val tempDisplay: TextView =view.findViewById(R.id.tempDisplay)
    val imageDisplay: ImageView =view.findViewById(R.id.imageDisplay)
}

 */