package com.example.foodproject.api

import com.example.foodproject.data.Restaurant
import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET("/api/restaurants/107257")
    suspend fun getRestaurant(): Response<Restaurant>
}