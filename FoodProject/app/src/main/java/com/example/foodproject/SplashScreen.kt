package com.example.foodproject

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodproject.repository.RetrofitRepository
import com.example.foodproject.util.Constants
import com.example.foodproject.viewmodel.RetrofitViewModel
import com.example.foodproject.viewmodel.RetrofitViewModelFactory
import kotlinx.coroutines.runBlocking


class SplashScreen : AppCompatActivity() {

    private lateinit var retrofitViewModel: RetrofitViewModel //viewmodel for retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val repository = RetrofitRepository() //api repo
        val viewModelFactory = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(this, viewModelFactory).get(RetrofitViewModel::class.java)

        if(isNetworkAvailable(this))
        {
            retrofitViewModel.getCountries()
            retrofitViewModel.getCities()

            retrofitViewModel.myResponsCountry.observe(this, Observer { response ->

                if(response.body() != null)
                {
                    //filling up utils class which contains every possible filter (state)
                    Constants.states = response.body()!!.countries as ArrayList<String>
                }

            })


            retrofitViewModel.myResponsCities.observe(this, Observer { response ->

                if(response.body() != null)
                {
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
        else{
            Toast.makeText(this,"There is no internet connection!",Toast.LENGTH_SHORT).show()
        }
    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}