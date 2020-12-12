package com.example.foodproject.api

import com.example.foodproject.model.CitiesResponse
import com.example.foodproject.model.CountriesResponse
import com.example.foodproject.model.Restaurant
import com.example.foodproject.model.RestaurantListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/restaurants")
    suspend fun getRestaurant(
            @Query("id") id : Int,
    ): Response<Restaurant>

    @GET("/countries")
    suspend fun getCountries(): Response<CountriesResponse>

    @GET("/cities")
    suspend fun getCities(): Response<CitiesResponse>

    @GET("/restaurants")
    suspend fun getRestaurantCountriesPage(
            @Query("state") state : String,
            @Query("page") page : Int
    ) : Response<RestaurantListResponse>

    @GET("/restaurants")
    suspend fun getRestaurantCitiesPage(
            @Query("city") city : String,
            @Query("page") page : Int
    ) : Response<RestaurantListResponse>


}