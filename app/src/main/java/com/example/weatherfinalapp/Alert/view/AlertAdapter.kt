package com.example.weatherfinalapp.Alert.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherfinalapp.R
import com.example.weatherfinalapp.model.Alert

class AlertAdapter(private val context: Context, private val listener: OnAlertClickListener) :
    ListAdapter<Alert, AlertAdapter.ViewHolder>(AlertAdapter.AlertDiffCallback()){

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater: LayoutInflater =
        parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.alert_list_card, parent, false)
    return ViewHolder(view)}

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val alertItem = getItem(position)
        val address = alertItem.address?.split(",")
        val locationName = address?.getOrNull(0) ?: ""  // Use index 1 to get the second part
        holder.location.text = locationName


        holder.dateText.text = alertItem.date
        holder.timeText.text = alertItem.time



        holder.deleteBtn.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Do You Want To Delete this Alert of $locationName?")
                .setPositiveButton("Ok") { dialog, which ->
                    listener.deleteAlert(alertItem)
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
        }
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deleteBtn: ImageView = itemView.findViewById(R.id.btn_delete_alert)
        val dateText: TextView = itemView.findViewById(R.id.tv_date_value_item_list)
        val timeText: TextView = itemView.findViewById(R.id.tv_time_value_item_list)
        val location: TextView = itemView.findViewById(R.id.tv_location_title)

    }


    class AlertDiffCallback : DiffUtil.ItemCallback<Alert>() {
        override fun areItemsTheSame(oldItem: Alert, newItem: Alert): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Alert,
            newItem: Alert
        ): Boolean {
            return oldItem == newItem
        }
    }


}

