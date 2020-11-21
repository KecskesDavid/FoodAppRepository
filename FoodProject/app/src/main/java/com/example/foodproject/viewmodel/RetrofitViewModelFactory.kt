package com.example.foodproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodproject.repository.RetrofitRepository

class RetrofitViewModelFactory(private val repository: RetrofitRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RetrofitViewModel(repository) as T
    }
}