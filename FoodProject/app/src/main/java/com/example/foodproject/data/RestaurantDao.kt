package com.example.foodproject.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodproject.model.FavoriteRestaurants
import com.example.foodproject.model.Restaurant
import com.example.foodproject.model.User

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRestaurant(restaurant: Restaurant)

    @Query("select * from restaurants")
    fun readAllData(): LiveData<List<Restaurant>>

    @Query("select * from restaurants where id = :id")
    fun readRestaurant(id: Int): LiveData<Restaurant>

    @Delete
    suspend fun deleteRestaurant(restaurant: Restaurant)

    @Update
    suspend fun updateRestaurant(restaurant: Restaurant)



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("select * from users")
    fun readAllDataFromUser(): LiveData<List<User>>

    @Query("select * from users where email = :email")
    fun readUserByEmail(email: String): LiveData<User>



    @Query("select restaurant_id from favorite_restaurants where user_id = :id")
    fun readFavoritesById(id: Int): LiveData<List<Long>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteRestaurants(favoriteRestaurants: FavoriteRestaurants)

    @Query("select * from favorite_restaurants")
    fun readAllDataFromFavorites(): LiveData<List<FavoriteRestaurants>>

    @Delete
    suspend fun deleteFavorites(favoriteRestaurants: FavoriteRestaurants)

}