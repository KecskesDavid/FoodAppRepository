package com.example.foodproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodproject.model.CitiesResponse
import com.example.foodproject.model.CountriesResponse
import com.example.foodproject.model.Restaurant
import com.example.foodproject.model.RestaurantListResponse
import com.example.foodproject.repository.RetrofitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class RetrofitViewModel(private val repository: RetrofitRepository): ViewModel() {

    val myRespons: MutableLiveData<Response<Restaurant>> = MutableLiveData()
    val myResponsCountry: MutableLiveData<Response<CountriesResponse>> = MutableLiveData()
    val myResponsCities: MutableLiveData<Response<CitiesResponse>> = MutableLiveData()
    val myResponsPage: MutableLiveData<Response<RestaurantListResponse>> = MutableLiveData()

    fun getRestaurant(id:Int){
        viewModelScope.launch(Dispatchers.Main){
            val response = repository.getRestaurant(id)
            myRespons.value = response
        }
    }

    fun getCountries(){
        viewModelScope.launch{
                val response = repository.getCountries()
                myResponsCountry.value = response
            }
    }

    fun getCities(){
        viewModelScope.launch{
            val response = repository.getCities()
            myResponsCities.value = response
        }
    }

    fun getRestaurantCountriesPage(state:String, page:Int)
    {
        viewModelScope.launch {
            val response = repository.getRestaurantCountriesPage(state,page)
            myResponsPage.value = response
        }
    }

    fun getRestaurantCitiesPage(city:String, page:Int)
    {
        viewModelScope.launch {
            val response = repository.getRestaurantCitiesPage(city,page)
            myResponsPage.value = response
        }
    }
}