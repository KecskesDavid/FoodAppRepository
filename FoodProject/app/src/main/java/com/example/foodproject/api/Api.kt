package com.example.foodproject.api

import com.example.foodproject.model.CountriesResponse
import com.example.foodproject.model.Restaurant
import com.example.foodproject.model.RestaurantListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/api/restaurants/107257")
    suspend fun getRestaurant(): Response<Restaurant>

    @GET("/api/countries")
    suspend fun getCountries(): Response<CountriesResponse>

    @GET("/api/restaurants")
    suspend fun getRestaurantPage(
            @Query("state") state : String,
            @Query("page") page : Int
    ) : Response<RestaurantListResponse>

}