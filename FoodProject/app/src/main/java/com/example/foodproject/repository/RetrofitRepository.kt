package com.example.foodproject.repository

import com.example.foodproject.api.RetrofitInstance
import com.example.foodproject.model.CountriesResponse
import com.example.foodproject.model.Restaurant
import retrofit2.Response

class RetrofitRepository {

    suspend fun getRestaurant(): Response<Restaurant> = RetrofitInstance.api.getRestaurant()

    suspend fun getCountries(): Response<CountriesResponse> = RetrofitInstance.api.getCountries()
}