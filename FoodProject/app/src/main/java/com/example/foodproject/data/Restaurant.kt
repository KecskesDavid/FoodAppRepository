package com.example.foodproject.data

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
    val reserve_url: URL,
    val mobile_reserve_url: URL,
    val image_url: URL
    )