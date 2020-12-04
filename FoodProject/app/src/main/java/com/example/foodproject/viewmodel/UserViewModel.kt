package com.example.foodproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.foodproject.data.AppDatabase
import com.example.foodproject.data.User
import com.example.foodproject.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    val readAllUsers: LiveData<List<User>>
    private val repository: RestaurantRepository

    init{
        val restaurantDao = AppDatabase.getDatabase(application).restaurantDao()
        repository = RestaurantRepository(restaurantDao)
        readAllUsers = repository.readAllUser
    }

    fun addUser(user: User)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun updateUser(user: User)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

}