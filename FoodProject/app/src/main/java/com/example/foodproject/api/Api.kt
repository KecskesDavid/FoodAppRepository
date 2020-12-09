package com.example.foodproject.api

import com.example.foodproject.model.CitiesResponse
import com.example.foodproject.model.CountriesResponse
import com.example.foodproject.model.Restaurant
import com.example.foodproject.model.RestaurantListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/api/restaurants")
    suspend fun getRestaurant(
            @Query("id") id : Int,
    ): Response<Restaurant>

    @GET("/api/countries")
    suspend fun getCountries(): Response<CountriesResponse>

    @GET("/api/cities")
    suspend fun getCities(): Response<CitiesResponse>

    @GET("/api/restaurants")
    suspend fun getRestaurantCountriesPage(
            @Query("state") state : String,
            @Query("page") page : Int
    ) : Response<RestaurantListResponse>

    @GET("/api/restaurants")
    suspend fun getRestaurantCitiesPage(
            @Query("city") city : String,
            @Query("page") page : Int
    ) : Response<RestaurantListResponse>


}