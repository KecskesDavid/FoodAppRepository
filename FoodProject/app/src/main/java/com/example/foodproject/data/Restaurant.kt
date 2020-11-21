package com.example.foodproject.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity(tableName = "restaurants")
data class Restaurant (
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val area: String,
    val postal_code: Int,
    val country: String,
    val phone: String,
    val lat: Double,
    val lng: Double,
    val price: Double,
    val reserve_url: String,
    val mobile_reserve_url: String,
    val image_url: String
    )