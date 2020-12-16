package com.example.foodproject.fragments.favorites

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodproject.R
import com.example.foodproject.adapters.RestaurantAdapter
import com.example.foodproject.model.Restaurant
import com.example.foodproject.repository.RetrofitRepository
import com.example.foodproject.util.Constants
import com.example.foodproject.util.Constants.Companion.idSP
import com.example.foodproject.viewmodel.FavoriteRestaurantsViewModel
import com.example.foodproject.viewmodel.RestaurantViewModel
import com.example.foodproject.viewmodel.RetrofitViewModel
import com.example.foodproject.viewmodel.RetrofitViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_favorites.view.*
import kotlinx.coroutines.runBlocking

class FravoritesFragment : Fragment() {

    private lateinit var favoriteRests: FavoriteRestaurantsViewModel
    private lateinit var restaurantsViewModel: RestaurantViewModel
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        favoriteRests = ViewModelProvider(this).get(FavoriteRestaurantsViewModel::class.java)
        restaurantsViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        val sharedPreferences = context?.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)

        val repository = RetrofitRepository()
        val viewModelFactory = RetrofitViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RetrofitViewModel::class.java)

        val adapter = RestaurantAdapter(requireContext())
        val recyclerViewRestaurant = view.favRestaurantRecView
        recyclerViewRestaurant.adapter = adapter
        recyclerViewRestaurant.layoutManager = LinearLayoutManager(requireContext())

        val listOfRestaurants = restaurantsViewModel.readAllRestaurants

        val user_id = sharedPreferences?.getInt(idSP, 0) !!

        if (user_id != 0) {
            val favoriteRestaurants = runBlocking { favoriteRests.readFavoritesById(sharedPreferences.getInt(idSP, 0)) }


            favoriteRestaurants.observe(viewLifecycleOwner, Observer { response ->

                response.forEach {
                    val restaurantsToShow = arrayListOf<Restaurant>()

                    listOfRestaurants.observe(viewLifecycleOwner, Observer { restaurant ->
                        for (i in restaurant) {
                            if (it == i.id) {
                                restaurantsToShow.add(i)
                                adapter.setData(restaurantsToShow)
                                break
                            }
                        }
                    })

                    Log.d("asdasd",restaurantsToShow.size.toString())
                }

            })
        }

        //this function is called every time when navigating to this page, sets placeholders in case the user is not loged in
        setUpPlaceHolder(view,user_id,recyclerViewRestaurant)

        //this function changes the visibility of the navigation bar after returning from the details page
        setUpBottomNav()

        return view
    }

    private fun setUpPlaceHolder(view: View, user_id: Int, recyclerViewRestaurant: RecyclerView) {
        val image_place_holder = view.findViewById<ImageView>(R.id.non_ImageView)
        val text_place_holder = view.findViewById<TextView>(R.id.non_TxtVew)

        if(user_id != 0)
        {
            image_place_holder?.visibility=View.GONE
            text_place_holder?.visibility=View.GONE
            recyclerViewRestaurant.visibility=View.VISIBLE
        }
        else
        {
            image_place_holder?.visibility=View.VISIBLE
            text_place_holder?.visibility=View.VISIBLE
            recyclerViewRestaurant.visibility=View.GONE

        }
    }

    private fun setUpBottomNav() {
        val navbar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navbar?.visibility = View.VISIBLE
    }
}