package com.example.foodproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val email: String,
    val address: String,
    val job: String,
    val phone: String,
    val password: String,
    val photo: String
){}