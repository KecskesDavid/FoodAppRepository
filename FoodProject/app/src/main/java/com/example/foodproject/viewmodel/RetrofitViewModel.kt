package com.example.foodproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodproject.data.Restaurant
import com.example.foodproject.repository.RetrofitRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RetrofitViewModel(private val repository: RetrofitRepository): ViewModel() {

    val myRespons: MutableLiveData<Response<Restaurant>> = MutableLiveData()

    fun getRestaurant(){
        viewModelScope.launch {
            val response = repository.getRestaurant()
            myRespons.value = response
        }
    }
}