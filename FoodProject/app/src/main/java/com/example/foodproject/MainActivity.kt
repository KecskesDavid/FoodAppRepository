package com.example.foodproject

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodproject.viewmodel.RestaurantViewModel
import com.example.foodproject.repository.RetrofitRepository
import com.example.foodproject.viewmodel.RetrofitViewModel
import com.example.foodproject.viewmodel.RetrofitViewModelFactory

class MainActivity : AppCompatActivity() {

    //viewmodel which stores all the restaurants
    private lateinit var mRestaurantViewModel: RestaurantViewModel
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        mRestaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        //todo: should be moved to splashscreen
        val repository = RetrofitRepository()
        val viewModelFactory = RetrofitViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(RetrofitViewModel::class.java)
        viewModel.getCountries()
        viewModel.getRestaurantPage("CA",1)

        viewModel.myResponsCountry.observe(this, Observer { response ->
            //todo: add to database
            val test = findViewById<TextView>(R.id.testText)
            var string: String = ""

            val states = mutableListOf<String>()
            response.body()!!.countries.forEach() { states.add(it) }

            states.forEach {
                var pageNr=1
                var ok = true
                while(ok)
                {
                    viewModel.getRestaurantPage(it, pageNr)
                    viewModel.myResponsPage.observe(this, Observer { resp ->

                        if( resp.body()?.restaurants?.size != 0  )
                        {
                            Log.d("resp", resp.body()?.restaurants?.get(0)?.address.toString())
                            //resp.body()?.restaurants?.forEach { mRestaurantViewModel.addRestaurant(it) }
                            ++pageNr
                        }
                        else
                        {
                            ok=false
                        }
                    })
                }
            }
        })



        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_restaurant_list, R.id.navigation_dashboard, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


}