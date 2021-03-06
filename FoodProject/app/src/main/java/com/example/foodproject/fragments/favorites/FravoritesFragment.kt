package com.example.foodproject.fragments.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodproject.R
import com.example.foodproject.adapters.RestaurantAdapter
import com.example.foodproject.fragments.details.DetailsFragment
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

class FravoritesFragment : Fragment() , RestaurantAdapter.OnItemClickListener// -> the one created in the adapter to migrate the functionalities to the fragments
{

    private lateinit var retrofitViewModel: RetrofitViewModel //viewmodel for retrofit
    private lateinit var restaurantViewModel: RestaurantViewModel //viewmodel for favorite table
    private lateinit var favoriteRestaurantViewModel: FavoriteRestaurantsViewModel //viewmodel for favorite table

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        val repository = RetrofitRepository()//api repo
        val viewModelFactory = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(this,viewModelFactory).get(RetrofitViewModel::class.java)
        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        favoriteRestaurantViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(FavoriteRestaurantsViewModel::class.java)
        val sharedPreferences = context?.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)



        val adapter = RestaurantAdapter(requireContext(),this)
        val recyclerViewRestaurant = view.favRestaurantRecView
        recyclerViewRestaurant.adapter = adapter
        recyclerViewRestaurant.layoutManager = LinearLayoutManager(requireContext())

        val listOfRestaurants = restaurantViewModel.readAllRestaurants //saving every restaurant from database

        val isActiveUser = sharedPreferences?.getInt(idSP, 0) ?: 0

        if (isActiveUser != 0) { //check if the user is loged in
            val favoriteRestaurants = runBlocking { favoriteRestaurantViewModel.readFavoritesById(isActiveUser) }  // taking every like restaurant by the loged in use

            favoriteRestaurants.observe(viewLifecycleOwner, Observer { likedRestaurantsIDs -> //observing these id-s

                listOfRestaurants.observe(viewLifecycleOwner, Observer { restaurants -> //then filtering the saved restaurants
                    val restaurantsToShow = arrayListOf<Restaurant>()

                    for (restaurant in restaurants) {
                        likedRestaurantsIDs.forEach { idOfRest ->
                            if ( idOfRest == restaurant.id ) {
                                restaurantsToShow.add(restaurant)
                            }
                        }
                    }

                    adapter.setData(restaurantsToShow) //finally added to recycler view
                })

            })
        }

        //this function is called every time when navigating to this page, sets placeholders in case the user is not loged in
        setUpPlaceHolder(view, isActiveUser, recyclerViewRestaurant)

        //this function changes the visibility of the navigation bar after returning from the details page
        setUpBottomNav()

        return view
    }

    private fun setUpPlaceHolder(view: View, user_id: Int, recyclerViewRestaurant: RecyclerView) {
        val image_place_holder = view.findViewById<ImageView>(R.id.non_ImageView)
        val text_place_holder = view.findViewById<TextView>(R.id.non_TxtVew)

        if (user_id != 0) {
            image_place_holder?.visibility = View.GONE
            text_place_holder?.visibility = View.GONE
            recyclerViewRestaurant.visibility = View.VISIBLE
        } else {
            image_place_holder?.visibility = View.VISIBLE
            text_place_holder?.visibility = View.VISIBLE
            recyclerViewRestaurant.visibility = View.GONE

        }
    }

    private fun setUpBottomNav() {
        val navbar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navbar?.visibility = View.VISIBLE
    }

    //delegation
    //overriding this function to implement the functionality of the adapter -> to navigate to other page
    override fun onItemClick(itemView: View, restaurant: Restaurant) {

        val bundle = Bundle() //for storing data of the current restaurant

        bundle.putParcelable(Constants.RESTAURANT_KEY, restaurant)

        val detailsFragment = DetailsFragment()
        detailsFragment.arguments = bundle

        //transaction to another fragment
        val transaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, detailsFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}