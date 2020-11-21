package com.example.foodproject.data

import androidx.lifecycle.LiveData
import com.example.foodproject.model.Restaurant

class RestaurantRepository(private val restaurantDao: RestaurantDao) {

    val readAllRestaurant: LiveData<List<Restaurant>> = restaurantDao.readAllData()

    suspend fun addRestaurant(restaurant: Restaurant)
    {
        restaurantDao.addRestaurant(restaurant)
    }
}