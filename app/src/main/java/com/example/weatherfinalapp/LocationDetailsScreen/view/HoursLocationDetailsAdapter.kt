package com.example.weatherfinalapp.LocationDetailsScreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.model.ListWeather
import java.text.SimpleDateFormat
import java.util.HashSet
import java.util.Locale

class HoursLocationDetailsAdapter(private val context: Context) : RecyclerView.Adapter<HoursLocationDetailsAdapter.ViewHolder>() {
    private var listOfLocationWeather = listOf<ListWeather>()


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
        listOfLocationWeather = filterList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.hour_loc_delatis_card,parent,false)
        return  ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfLocationWeather.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todayForeCast = listOfLocationWeather[position]
        holder.time.text = todayForeCast.dt_txt!!.substring(11,16).toString()
        val temperatureFahrenheit = todayForeCast.main?.temp
        val temperatureCelsuis = (temperatureFahrenheit?.minus(273.15))
        val temperatureFormatted = String.format("%.2f" , temperatureCelsuis)

        if (todayForeCast.weather.isNotEmpty()) {
            val weather = todayForeCast.weather[0] // Assuming you're only interested in the first weather condition
            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${weather.icon}.png")
                .into(holder.image)
        }

        holder.temp.text = "$temperatureFormatted°c"

        holder.time.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(todayForeCast.dt_txt)
            ?.let {
                SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                ).format(it)
            }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_hourly_card_loc_dets)
        val time: TextView = itemView.findViewById(R.id.tv_time_hourly_card_loc_dets)
        val temp: TextView = itemView.findViewById(R.id.tv_temp_hourly_card_loc_dets)

    }
}

/*
class HoursLocationDetailsAdapter(private val context: Context) : RecyclerView.Adapter<HoursLocationDetailsAdapter.ViewHolder>() {
    private var listOfLocationWeather = listOf<ListWeather>()

    // Function to set the list of weather data
    fun setList(newList: List<ListWeather>) {
        listOfLocationWeather = newList
        notifyDataSetChanged() // Notify adapter that data set has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.hour_loc_delatis_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfLocationWeather.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todayForeCast = listOfLocationWeather[position]
        holder.time.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(todayForeCast.dt_txt)
            ?.let {
                SimpleDateFormat("hh:mm a", Locale.getDefault()).format(it)
            }

        val temperatureCelsius = todayForeCast.main?.temp?.minus(273.15) // Convert temperature to Celsius
        val temperatureFormatted = String.format("%.2f", temperatureCelsius)
        holder.temp.text = "$temperatureFormatted°C"

        if (todayForeCast.weather.isNotEmpty()) {
            val weather = todayForeCast.weather[0] // Assuming you're only interested in the first weather condition
            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${weather.icon}.png")
                .into(holder.image)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_hourly_card_loc_dets)
        val time: TextView = itemView.findViewById(R.id.tv_time_hourly_card_loc_dets)
        val temp: TextView = itemView.findViewById(R.id.tv_temp_hourly_card_loc_dets)
    }
}

 */