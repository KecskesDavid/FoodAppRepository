package com.example.foodproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodproject.model.CountriesResponse
import com.example.foodproject.model.Restaurant
import com.example.foodproject.model.RestaurantListResponse
import com.example.foodproject.repository.RetrofitRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class RetrofitViewModel(private val repository: RetrofitRepository): ViewModel() {

    val myRespons: MutableLiveData<Response<Restaurant>> = MutableLiveData()
    val myResponsCountry: MutableLiveData<Response<CountriesResponse>> = MutableLiveData()
    val myResponsPage: MutableLiveData<Response<RestaurantListResponse>> = MutableLiveData()

    fun getRestaurant(){
        viewModelScope.launch {
            val response = repository.getRestaurant()
            myRespons.value = response
        }
    }

    fun getCountries(){
        viewModelScope.launch {
            val response = repository.getCountries()
            myResponsCountry.value = response
        }
    }

    fun getRestaurantPage(state:String, page:Int)
    {
        viewModelScope.launch {
            val response = repository.getRestaurantPage(state,page)
            myResponsPage.value = response
        }
    }
}