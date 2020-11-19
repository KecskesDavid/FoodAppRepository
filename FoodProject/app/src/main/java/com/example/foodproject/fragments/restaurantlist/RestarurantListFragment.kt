package com.example.foodproject.fragments.restaurantlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodproject.R

class RestarurantListFragment : Fragment() {

    private lateinit var restaurantListViewModel: RestaurantListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        restaurantListViewModel =
                ViewModelProvider(this).get(RestaurantListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_restaurant_list, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        restaurantListViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}