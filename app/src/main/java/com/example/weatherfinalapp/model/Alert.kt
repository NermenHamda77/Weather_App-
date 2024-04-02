package com.example.weatherfinalapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "alert")
data class Alert(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    val date:String,
    val time:String,
    val alertType:String,
    val latitude: Double,
    val longitude: Double,
    val address: String?,
): Serializable
