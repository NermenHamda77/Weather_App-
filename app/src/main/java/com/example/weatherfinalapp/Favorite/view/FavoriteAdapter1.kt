package com.example.weatherfinalapp.Favorite.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.model.FavoriteLocation

class FavoriteAdapter1(private val context: Context, private val listener: OnFavClickListener) :
    ListAdapter<FavoriteLocation, FavoriteAdapter1.FavViewHolder>(FavoriteAdapter1.WeatherDiffCallback()) {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
            val inflater: LayoutInflater =
                parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.fav_locations_card, parent, false)
            return FavViewHolder(view)
        }


        override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
            val locationItem =  getItem(position)
            val address = locationItem.address?.split(",")
            val locationName = address?.getOrNull(0) ?: ""  // Use index 1 to get the second part
            holder.name.text = locationName




            holder.deleteImg.setOnClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("Do You Want To Delete this Location?")
                    .setPositiveButton("Ok") { dialog, which ->
                        listener.deleteLocation(locationItem)
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    .setCancelable(false)
                    .show()
            }


            holder.card.setOnClickListener {
                listener.showFavLocationDetails(locationItem)
            }


        }

        inner class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val deleteImg: ImageView = itemView.findViewById(R.id.iv_delete_fav_location_fav_list)
            val name: TextView = itemView.findViewById(R.id.tv_location_name_fav_list)
            val card: CardView = itemView.findViewById(R.id.fav_location_card)

        }

        class WeatherDiffCallback : DiffUtil.ItemCallback<FavoriteLocation>() {
            override fun areItemsTheSame(oldItem: FavoriteLocation, newItem: FavoriteLocation): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FavoriteLocation,
                newItem: FavoriteLocation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


/*
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
 */