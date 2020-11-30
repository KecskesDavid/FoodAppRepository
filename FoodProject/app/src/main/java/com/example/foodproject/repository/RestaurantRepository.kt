package com.example.foodproject.repository

import androidx.lifecycle.LiveData
import com.example.foodproject.data.FavoriteRestaurants
import com.example.foodproject.data.RestaurantDao
import com.example.foodproject.data.Restaurant

class RestaurantRepository(private val restaurantDao: RestaurantDao) {

    val readAllRestaurant: LiveData<List<Restaurant>> = restaurantDao.readAllData()

    suspend fun addRestaurant(restaurant: Restaurant)
    {
        restaurantDao.addRestaurant(restaurant)
    }

    val readAllFavoriteRestaurantsRestaurant: LiveData<List<FavoriteRestaurants>> = restaurantDao.readAllDataFromFavorites()

    suspend fun addFavoriteRestaurants(favoriteRestaurants: FavoriteRestaurants)
    {
        restaurantDao.addFavoriteRestaurants(favoriteRestaurants)
    }
}