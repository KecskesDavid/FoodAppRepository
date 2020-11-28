package com.example.foodproject.repository

import com.example.foodproject.api.RetrofitInstance
import com.example.foodproject.model.CitiesResponse
import com.example.foodproject.model.CountriesResponse
import com.example.foodproject.model.Restaurant
import com.example.foodproject.model.RestaurantListResponse
import retrofit2.Response

class RetrofitRepository {

    suspend fun getRestaurant(): Response<Restaurant> = RetrofitInstance.api.getRestaurant()

    suspend fun getCountries(): Response<CountriesResponse> = RetrofitInstance.api.getCountries()

    suspend fun getCities(): Response<CitiesResponse> = RetrofitInstance.api.getCities()

    suspend fun getRestaurantCountriesPage(state: String, page: Int): Response<RestaurantListResponse> = RetrofitInstance.api.getRestaurantCountriesPage(state,page)

    suspend fun getRestaurantCitiesPage(city: String, page: Int): Response<RestaurantListResponse> = RetrofitInstance.api.getRestaurantCitiesPage(city,page)
}