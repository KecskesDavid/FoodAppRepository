package com.example.foodproject.fragments.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

        val user_id = sharedPreferences?.getInt(idSP, 0)

        if (user_id != 0) {
            val favoriteRestaurants = runBlocking { favoriteRests.readFavoritesById(sharedPreferences!!.getInt(idSP, 0)) }

            val restaurantsToShow = arrayListOf<Restaurant>()

            favoriteRestaurants.observe(viewLifecycleOwner, Observer { response ->

                response.forEach {
                    listOfRestaurants.observe(viewLifecycleOwner, Observer { restaurant ->
                        for (i in restaurant) {
                            if (it == i.id) {
                                restaurantsToShow.add(i)
                                adapter.setData(restaurantsToShow)
                                break
                            }
                        }
                    })
                }


            })

        }


//        favoriteRests.readAllFavoriteRestaurants.observe(viewLifecycleOwner, Observer { response ->
//            val restaurantsToShow = arrayListOf<Restaurant>()
//
//            val user_id = sharedPreferences?.getInt(idSP, 0)
//
//            if (user_id != 0) {
//                response.forEach {
//
//                    listOfRestaurants.observe(viewLifecycleOwner, Observer { restaurant ->
//                        for (i in restaurant) {
//                            if (it.restaurant_id == i.id && it.user_id == user_id) {
//                                restaurantsToShow.add(i)
//                                adapter.setData(restaurantsToShow)
//                                break
//                            }
//                        }
//                    })
//
//                }
//            }
//
//        })

        setUpBottomNav()

        return view
    }

    private fun setUpBottomNav() {
        val navbar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navbar?.visibility = View.VISIBLE
    }
}