package com.example.foodproject.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodproject.adapters.RestaurantAdapter
import kotlinx.android.parcel.Parcelize
import java.net.URL

@Entity(tableName = "restaurants")
@Parcelize
data class Restaurant (
    @PrimaryKey(autoGenerate = false)
//    val id: Int,
    val id: Long,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val area: String,
    val postal_code: String,
    val country: String,
    val phone: String,
    val lat: Double,
    val lng: Double,
    val price: Int,
    val reserve_url: String,
    val mobile_reserve_url: String,
    val image_url: String
    ): Parcelable
