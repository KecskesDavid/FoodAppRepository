package com.example.foodproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodproject.repository.RetrofitRepository
import com.example.foodproject.util.Constants
import com.example.foodproject.viewmodel.RetrofitViewModel
import com.example.foodproject.viewmodel.RetrofitViewModelFactory
import kotlinx.coroutines.runBlocking


class SplashScreen : AppCompatActivity() {

//    private lateinit var mRestaurantViewModel: RestaurantViewModel
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

//        mRestaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        val repository = RetrofitRepository()
        val viewModelFactory = RetrofitViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RetrofitViewModel::class.java)
        viewModel.getCountries()
        viewModel.getCities()

        viewModel.myResponsCountry.observe(this, Observer { response ->

            if(response.body() != null)
            {
                val states = arrayListOf<String>()
                response.body()!!.countries.forEach() { states.add(it)
                    Log.d("logthisout",it)
                }

                //filling up utils class which contains every possible filter (state)
                Constants.states = states
            }

        })

        
        viewModel.myResponsCities.observe(this, Observer { response ->

            if(response.body() != null)
            {
//                Log.d("logthisout","ASDdd")
//
//                val cities = arrayListOf<String>()
//                response.body()!!.cities.forEach() { cities.add(it)
//                Log.d("logthisout",it)
//                }

                //filling up utils class which contains every possible filter (cities)
                Constants.cities = response.body()!!.cities as ArrayList<String>
            }

        })


        //delay for the splash screen
        Handler().postDelayed({
            super.onPostResume()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}