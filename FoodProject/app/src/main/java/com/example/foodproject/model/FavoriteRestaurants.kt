package com.example.foodproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_restaurants")
data class FavoriteRestaurants(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val restaurant_id: Int,
    val user_id: Int,
) {
}