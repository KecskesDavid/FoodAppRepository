package com.example.foodproject.api

import com.example.foodproject.model.CountriesResponse
import com.example.foodproject.model.Restaurant
import retrofit2.Response
import retrofit2.http.GET

interface Api {

    @GET("/api/restaurants/107257")
    suspend fun getRestaurant(): Response<Restaurant>

    @GET("/api/countries")
    suspend fun getCountries(): Response<CountriesResponse>
}