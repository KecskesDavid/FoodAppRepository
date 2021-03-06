package com.example.foodproject.repository

import androidx.lifecycle.LiveData
import com.example.foodproject.model.FavoriteRestaurants
import com.example.foodproject.data.RestaurantDao
import com.example.foodproject.model.Restaurant
import com.example.foodproject.model.User

class RestaurantRepository(private val restaurantDao: RestaurantDao) {

    //Functions for reading from Restaurant table
    val readAllRestaurant: LiveData<List<Restaurant>> = restaurantDao.readAllData()

    suspend fun addRestaurant(restaurant: Restaurant)
    {
        restaurantDao.addRestaurant(restaurant)
    }

    suspend fun deleteRestaurant(restaurant: Restaurant)
    {
        restaurantDao.deleteRestaurant(restaurant)
    }

    suspend fun updateRestaurant(restaurant: Restaurant)
    {
        restaurantDao.updateRestaurant(restaurant)
    }

    suspend fun readRestaurant(id: Int) = restaurantDao.readRestaurant(id)



    //Functions for reading from User table
    val readAllUser: LiveData<List<User>> = restaurantDao.readAllDataFromUser()

    suspend fun addUser(user: User)
    {
        restaurantDao.addUser(user)
    }

    suspend fun updateUser(user: User)
    {
        restaurantDao.updateUser(user)
    }

    suspend fun readUserByEmail(email: String) = restaurantDao.readUserByEmail(email)



    //Functions for reading from Favorites table
    val readAllFavoriteRestaurantsRestaurant: LiveData<List<FavoriteRestaurants>> = restaurantDao.readAllDataFromFavorites()

    suspend fun readFavoritesById( id: Int ) = restaurantDao.readFavoritesById(id)

    suspend fun addFavoriteRestaurants(favoriteRestaurants: FavoriteRestaurants)
    {
        restaurantDao.addFavoriteRestaurants(favoriteRestaurants)
    }

    suspend fun deleteFavorites(favoriteRestaurants: FavoriteRestaurants)
    {
        restaurantDao.deleteFavorites(favoriteRestaurants)
    }



}