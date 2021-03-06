package com.example.foodproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.foodproject.data.AppDatabase
import com.example.foodproject.repository.RestaurantRepository
import com.example.foodproject.model.Restaurant
import com.example.foodproject.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantViewModel (application: Application): AndroidViewModel(application) {

    val readAllRestaurants: LiveData<List<Restaurant>>
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

    fun updateRestaurant(restaurant: Restaurant)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateRestaurant(restaurant)
        }
    }


    suspend fun readRestaurant(id: Int): LiveData<Restaurant> = withContext(viewModelScope.coroutineContext) { repository.readRestaurant(id) }

}