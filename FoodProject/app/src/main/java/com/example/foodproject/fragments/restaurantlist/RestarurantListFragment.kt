package com.example.foodproject.fragments.restaurantlist

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
import com.example.foodproject.viewmodel.RestaurantViewModel
import kotlinx.android.synthetic.main.fragment_restaurant_list.view.*

class RestarurantListFragment : Fragment() {

    private lateinit var restaurantListViewModel: RestaurantListViewModel
    private lateinit var restaurantViewModel: RestaurantViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        restaurantListViewModel =
                ViewModelProvider(this).get(RestaurantListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_restaurant_list, container, false)

        //RecyclerView with the list of Restaurants
        val adapter = RestaurantAdapter()
        val recyclerViewRestaurant = root.restaurantRecView
        recyclerViewRestaurant.adapter=adapter
        recyclerViewRestaurant.layoutManager=LinearLayoutManager(requireContext())

        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        restaurantViewModel.readAllRestaurants.observe(viewLifecycleOwner, Observer {rest ->
            adapter.setData(rest)
        })

        return root
    }
}