package com.example.weatherfinalapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteLocation")
data class FavoriteLocation(
    val latitude: Double,
    val longtiude: Double,
    val address: String?,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longtiude)
        parcel.writeString(address)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoriteLocation> {
        override fun createFromParcel(parcel: Parcel): FavoriteLocation {
            return FavoriteLocation(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteLocation?> {
            return arrayOfNulls(size)
        }
    }
}
