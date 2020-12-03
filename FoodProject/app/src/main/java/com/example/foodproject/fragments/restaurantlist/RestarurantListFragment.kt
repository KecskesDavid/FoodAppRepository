package com.example.foodproject.fragments.restaurantlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodproject.R
import com.example.foodproject.adapters.RestaurantAdapter
import com.example.foodproject.data.Restaurant
import com.example.foodproject.repository.RetrofitRepository
import com.example.foodproject.util.Constants
import com.example.foodproject.viewmodel.RestaurantViewModel
import com.example.foodproject.viewmodel.RetrofitViewModel
import com.example.foodproject.viewmodel.RetrofitViewModelFactory
import kotlinx.android.synthetic.main.fragment_restaurant_list.view.*

class RestarurantListFragment : Fragment() {

    private lateinit var restaurantViewModel: RestaurantViewModel //for database
    private lateinit var viewModel: RetrofitViewModel //for retrofit

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_list, container, false)

        //Filling the spinner with the filters, countries, cities
        val spinnerState: Spinner = view.findViewById(R.id.countryFilter)
        val myAdapterState = activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, Constants.states)
        }
        spinnerState.adapter = myAdapterState

        val spinnerCity: Spinner = view.findViewById(R.id.cityFilter)
        val myAdapterCity = activity?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, Constants.cities) }
        spinnerCity.adapter = myAdapterCity


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


        //filter for states
        val filterStateBtn : Button = view.findViewById(R.id.filterStateBtn)
        filterStateBtn.setOnClickListener {

            //get the filter state
            val state = spinnerState.selectedItem.toString()

            val restaurantsToShow = arrayListOf<Restaurant>()

            //todo get every restaurant not only 5 pages
            //viewModel.getRestaurantPage(state, 0)
            //val numOfRestaurantsviewModel = viewModel.myResponsPage.value?.body()?.total_entries?.div(25) !!

            for( i in 1..5 ) {

                //todo: syncronization problem
                viewModel.getRestaurantCountriesPage(state, i)

                viewModel.myResponsPage.value?.body()?.restaurants?.forEach{
                    restaurantsToShow.add(it)
                }

            }

            if(restaurantsToShow.size == 0) {
                Toast.makeText(context,"There is no restaurants in this state!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,restaurantsToShow.size.toString()+" restaurants listed!",Toast.LENGTH_SHORT).show()
                adapter.setData(restaurantsToShow)
            }
        }

        //filter for cities
        val filterCityBtn : Button = view.findViewById(R.id.filterCityBtn)
        filterCityBtn.setOnClickListener {

            //get the filter state
            val city = spinnerCity.selectedItem.toString()

            val restaurantsToShow = arrayListOf<Restaurant>()

            //todo get every restaurant not only 5 pages
            //viewModel.getRestaurantPage(state, 0)
            //val numOfRestaurantsviewModel = viewModel.myResponsPage.value?.body()?.total_entries?.div(25) !!

           // for( i in 1..3 ) {

                //todo: syncronization problem
                viewModel.getRestaurantCitiesPage(city, 1)

                viewModel.myResponsPage.value?.body()?.restaurants?.forEach{
                    restaurantsToShow.add(it)
                }

           // }

            if(restaurantsToShow.size == 0) {
                Toast.makeText(context,"There is no restaurants in this city!",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context,restaurantsToShow.size.toString()+" restaurants listed!",Toast.LENGTH_SHORT).show()
                adapter.setData(restaurantsToShow)
            }
        }

        return view
    }
}