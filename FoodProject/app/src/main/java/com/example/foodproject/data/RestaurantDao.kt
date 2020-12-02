package com.example.foodproject.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addRestaurant(restaurant: Restaurant)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFavoriteRestaurants(favoriteRestaurants: FavoriteRestaurants)

    @Query("select * from restaurants")
    fun readAllData(): LiveData<List<Restaurant>>

    @Query("select * from favorite_restaurants")
    fun readAllDataFromFavorites(): LiveData<List<FavoriteRestaurants>>

    @Delete
    suspend fun deleteRestaurant(restaurant: Restaurant)
}