package com.example.foodproject.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodproject.model.Restaurant

@Dao
interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRestaurant(restaurant: Restaurant)


    @Query("select * from restaurants")
    fun readAllData(): LiveData<List<Restaurant>>
}