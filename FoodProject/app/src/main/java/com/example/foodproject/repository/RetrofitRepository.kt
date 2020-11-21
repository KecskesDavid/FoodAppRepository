package com.example.foodproject.repository

import com.example.foodproject.api.RetrofitInstance
import com.example.foodproject.data.Restaurant
import retrofit2.Response

class RetrofitRepository {

    suspend fun getRestaurant(): Response<Restaurant>{
        return RetrofitInstance.api.getRestaurant()
    }
}