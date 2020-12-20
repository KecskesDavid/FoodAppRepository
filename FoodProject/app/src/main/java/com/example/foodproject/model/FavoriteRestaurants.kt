package com.example.foodproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_restaurants")
data class FavoriteRestaurants(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val restaurant_id: Long,
    val user_id: Int,
) {
}