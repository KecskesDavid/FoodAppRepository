package com.example.foodproject.fragments.restaurantlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.example.foodproject.util.Constants.Companion.cities
import com.example.foodproject.viewmodel.RestaurantViewModel
import com.example.foodproject.viewmodel.RetrofitViewModel
import com.example.foodproject.viewmodel.RetrofitViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_restaurant_list.view.*
import kotlinx.coroutines.runBlocking

class RestarurantListFragment : Fragment(){
    //todo try so solve pages with spinner
    //todo try test everything
    //todo make beautiful code
    private lateinit var restaurantViewModel: RestaurantViewModel //for database
    private lateinit var viewModel: RetrofitViewModel //for retrofit

    val restaurantsToShow = arrayListOf<Restaurant>()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_list, container, false)

        //Filling up spinner with cities
        val spinnerCity: Spinner = view.findViewById(R.id.cityFilter)
        val buttonGo: Button = view.findViewById(R.id.buttonGo)
        val myAdapterCity = activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, Constants.cities) }
        spinnerCity.adapter = myAdapterCity

        val spinnerPageNr: Spinner = view.findViewById(R.id.pageNrSpinner)
        val list = (1..100).toList()
        val myAdapterPage = activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list ) }
        spinnerPageNr.adapter = myAdapterPage


        //RecyclerView with the list of Restaurants
        val adapter = RestaurantAdapter(requireContext())
        val recyclerViewRestaurant = view.restaurantRecView
        recyclerViewRestaurant.adapter=adapter
        recyclerViewRestaurant.layoutManager=LinearLayoutManager(requireContext())


        //filter the data by state
        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        val repository = RetrofitRepository()
        val viewModelFactory = RetrofitViewModelFactory(repository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(RetrofitViewModel::class.java)

        var flag=false

        //this function is called every time when navigating to this page, sets placeholders in case there are no restaurants to show
        setUpPlaceHolder(view,flag,recyclerViewRestaurant)

        //to set up first navigation
        viewModel.getRestaurantCitiesPage(cities[0], 1)

        viewModel.myResponsPage.observe(viewLifecycleOwner, Observer { response ->

            flag = response.body()?.restaurants?.size != 0

            //this function should be called for every click and filter
            setUpPlaceHolder(view,flag,recyclerViewRestaurant)

            adapter.setData(response.body()?.restaurants!!)

        })

        buttonGo.setOnClickListener {

            val city = spinnerCity.selectedItem.toString()
            val page = (spinnerPageNr.selectedItem ?: 1) as Int

            viewModel.getRestaurantCitiesPage(city, page)

        }
        
        //this function changes the visibility of the navigation bar after returning from the details page
        setUpBottomNav()

        return view
    }

    private fun setUpPlaceHolder(view: View, flag: Boolean,recyclerViewRestaurant: RecyclerView) {
        val image_place_holder = view.findViewById<ImageView>(R.id.non_ImageView)
        val text_place_holder = view.findViewById<TextView>(R.id.non_TxtVew)

        if(flag)
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