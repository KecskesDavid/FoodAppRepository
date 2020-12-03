package com.example.foodproject.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRestaurant(restaurant: Restaurant)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteRestaurants(favoriteRestaurants: FavoriteRestaurants)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("select * from restaurants")
    fun readAllData(): LiveData<List<Restaurant>>

    @Query("select restaurant_id from favorite_restaurants where user_id = :id")
    fun readFavoritesById(id: Int): LiveData<List<Int>>

    @Query("select * from users")
    fun readAllDataFromUser(): LiveData<List<User>>

    @Query("select * from favorite_restaurants")
    fun readAllDataFromFavorites(): LiveData<List<FavoriteRestaurants>>

    @Delete
    suspend fun deleteRestaurant(restaurant: Restaurant)

    @Delete
    suspend fun deleteFavorites(favoriteRestaurants: FavoriteRestaurants)

}