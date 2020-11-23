package com.example.foodproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.foodproject.repository.RetrofitRepository
import com.example.foodproject.viewmodel.RestaurantViewModel
import com.example.foodproject.viewmodel.RetrofitViewModel
import com.example.foodproject.viewmodel.RetrofitViewModelFactory

class SplashScreen : AppCompatActivity() {

    private lateinit var mRestaurantViewModel: RestaurantViewModel
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mRestaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        //todo: should be moved to splashscreen
        val repository = RetrofitRepository()
        val viewModelFactory = RetrofitViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(RetrofitViewModel::class.java)
        viewModel.getCountries()
        viewModel.getRestaurantPage("CA",1)

        viewModel.myResponsCountry.observe(this, Observer { response ->
            //todo: add to database

            val states = mutableListOf<String>()
            response.body()!!.countries.forEach() { states.add(it) }

            states.forEach {
                //var pageNr=1
                //var ok = true
                //while(ok)

                viewModel.getRestaurantPage(it, 1)
                viewModel.myResponsPage.observe(this, Observer { resp ->

                    if( resp.body()?.restaurants?.size != 0  )
                    {
                        Log.d("resp", resp.body()?.restaurants?.get(0)?.address.toString())
                        //resp.body()?.restaurants?.forEach { mRestaurantViewModel.addRestaurant(it) }
                        //++pageNr
                    }
                    //  else
                    {
                        //   ok=false
                    }
                })

            }
        })


        Handler().postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },3000)
    }

    //to make from url bitmap
//    private fun toBitmap(url: String): Bitmap
//    {
//        val loading = ImageLoader(requireContext())
//        val request = ImageRequest.Builder(requireContext())
//                .data(url)
//                .build()
//
//        val result = (loading.execute(request) as SuccessResult).drawable
//        return (result as BitmapDrawable).bitmap
//    }
}