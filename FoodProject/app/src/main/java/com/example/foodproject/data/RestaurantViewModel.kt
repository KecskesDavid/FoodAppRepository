package com.example.foodproject.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantViewModel (application: Application): AndroidViewModel(application) {

    private val readAllRestaurants: LiveData<List<Restaurant>>
    private val repository: RestaurantRepository

    init{
        val restaurantDao = AppDatabase.getDatabase(application).restaurantDao()
        repository = RestaurantRepository(restaurantDao)
        readAllRestaurants = repository.readAllRestaurant
    }

    fun addRestaurant(restaurant: Restaurant)
    {
        viewModelScope.launch(Dispatchers.IO){
            repository.addRestaurant(restaurant)
        }
    }
}