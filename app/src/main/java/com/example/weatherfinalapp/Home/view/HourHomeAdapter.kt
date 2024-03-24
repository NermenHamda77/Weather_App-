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
import java.util.Locale

class HourHomeAdapter(private val context: Context) :
    ListAdapter<ListWeather, HourHomeAdapter.ViewHolder>(WeatherDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.hourly_weather_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = getItem(position)

        Glide.with(context).load("https://openweathermap.org/img/wn/${weather.weather[0].icon}.png")
            .into(holder.image)

        //Glide.with(context).load(weather.weather.get(position).icon).into(holder.image)
        holder.temp.text = "${weather.main.temp.toString()}°C"
        holder.time.text = SimpleDateFormat(
            "hh:mm a",
            Locale.getDefault()
        ).format(SimpleDateFormat("yyyy-MM-dd HH::mm", Locale.getDefault()))
        //Get the current time
        /*holder.time.text =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(weather.dt_txt)?.let {
                SimpleDateFormat(
                    "hh:mm a",
                    Locale.getDefault()
                ).format(it)
            }*/

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.iv_hourly_card)
        val time: TextView = itemView.findViewById(R.id.tv_time_hourly_card)
        val temp: TextView = itemView.findViewById(R.id.tv_temp_hourly_card)

    }

    class WeatherDiffCallback : DiffUtil.ItemCallback<ListWeather>() {
        override fun areItemsTheSame(oldItem: ListWeather, newItem: ListWeather): Boolean {
            return oldItem.weather[0].id == newItem.weather[0].id                ///??????
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
class ProductListAdapter(private val context: Context, private val listener: OnAllProductClickListener) :
    ListAdapter<Product, ProductListAdapter.ViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)

        Log.d("Image URL", product.thumbnail)
        Glide.with(context).load(product.thumbnail).into(holder.img)
        holder.title.text = product.title
        holder.price.text = product.price.toString()
        holder.brand.text = product.brand
        holder.desc.text = product.description
        holder.rate.rating = product.rating.toFloat()
        holder.addFav.setOnClickListener {
            listener.addProduct(product)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.img)
        val title: TextView = itemView.findViewById(R.id.textTitle)
        val price: TextView = itemView.findViewById(R.id.textPrice)
        val brand: TextView = itemView.findViewById(R.id.textBrand)
        val desc: TextView = itemView.findViewById(R.id.textDesc)
        val rate: RatingBar = itemView.findViewById(R.id.ratingBar)
        val addFav: Button = itemView.findViewById(R.id.btnFavAdd)
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}

 */