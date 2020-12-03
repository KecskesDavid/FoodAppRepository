package com.example.foodproject.repository

import androidx.lifecycle.LiveData
import com.example.foodproject.data.FavoriteRestaurants
import com.example.foodproject.data.RestaurantDao
import com.example.foodproject.data.Restaurant
import com.example.foodproject.data.User

class RestaurantRepository(private val restaurantDao: RestaurantDao) {

    val readAllRestaurant: LiveData<List<Restaurant>> = restaurantDao.readAllData()

    suspend fun addRestaurant(restaurant: Restaurant)
    {
        restaurantDao.addRestaurant(restaurant)
    }

    val readAllFavoriteRestaurantsRestaurant: LiveData<List<FavoriteRestaurants>> = restaurantDao.readAllDataFromFavorites()

    suspend fun readFavoritesById( id: Int ): LiveData<List<Int>> = restaurantDao.readFavoritesById(id)

    suspend fun addFavoriteRestaurants(favoriteRestaurants: FavoriteRestaurants)
    {
        restaurantDao.addFavoriteRestaurants(favoriteRestaurants)
    }

    suspend fun deleteFavorites(favoriteRestaurants: FavoriteRestaurants)
    {
        restaurantDao.deleteFavorites(favoriteRestaurants)
    }

    suspend fun deleteRestaurant(restaurant: Restaurant)
    {
        restaurantDao.deleteRestaurant(restaurant)
    }

    val readAllUser: LiveData<List<User>> = restaurantDao.readAllDataFromUser()

    suspend fun addUser(user: User)
    {
        restaurantDao.addUser(user)
    }


}