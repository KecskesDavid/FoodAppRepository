package com.example.foodproject.repository

import com.example.foodproject.api.RetrofitInstance
import com.example.foodproject.model.CountriesResponse
import com.example.foodproject.model.Restaurant
import com.example.foodproject.model.RestaurantListResponse
import retrofit2.Response

class RetrofitRepository {

    suspend fun getRestaurant(): Response<Restaurant> = RetrofitInstance.api.getRestaurant()

    suspend fun getCountries(): Response<CountriesResponse> = RetrofitInstance.api.getCountries()

    suspend fun getRestaurantPage(state: String, page: Int): Response<RestaurantListResponse> = RetrofitInstance.api.getRestaurantPage(state,page)
}