package com.example.weatherfinalapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "alert")
data class Alert(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    val date:String,
    val fromTime:String,
    val toTime:String,
    val alertType:String,
    val latitude: Double,
    val longitude: Double,
    val address: String?,
): Serializable

/*
import android.os.Parcel
import android.os.Parcelable

data class Alert(
    val date: String?,
    val fromTime: String?,
    val toTime: String?,
    val alertType: String?,
    val address: String?,
    val latitude: Double,
    val longitude: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(fromTime)
        parcel.writeString(toTime)
        parcel.writeString(alertType)
        parcel.writeString(address)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Alert> {
        override fun createFromParcel(parcel: Parcel): Alert {
            return Alert(parcel)
        }

        override fun newArray(size: Int): Array<Alert?> {
            return arrayOfNulls(size)
        }
    }
}

 */