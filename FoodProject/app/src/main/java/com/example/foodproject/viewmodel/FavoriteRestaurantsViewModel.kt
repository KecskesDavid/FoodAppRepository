package com.example.foodproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.foodproject.data.AppDatabase
import com.example.foodproject.data.FavoriteRestaurants
import com.example.foodproject.data.Restaurant
import com.example.foodproject.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteRestaurantsViewModel(application: Application): AndroidViewModel(application) {

    val readAllFavoriteRestaurants: LiveData<List<FavoriteRestaurants>>
    private val repository: RestaurantRepository

    init{
        val restaurantDao = AppDatabase.getDatabase(application).restaurantDao()
        repository = RestaurantRepository(restaurantDao)
        readAllFavoriteRestaurants = repository.readAllFavoriteRestaurantsRestaurant
    }

    fun addFavoriteRestaurants(favoriteRestaurants: FavoriteRestaurants)
    {
        viewModelScope.launch(Dispatchers.IO){
            repository.addFavoriteRestaurants(favoriteRestaurants)
        }
    }

}