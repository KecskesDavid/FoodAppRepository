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
import com.example.foodproject.R
import com.example.foodproject.adapters.RestaurantAdapter
import com.example.foodproject.model.Restaurant
import com.example.foodproject.repository.RetrofitRepository
import com.example.foodproject.util.Constants
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


        //filter for states
//        spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                val city = parent.getItemAtPosition(position).toString()
//
//                val restaurantsToShow = arrayListOf<Restaurant>()
//
//                viewModel.getRestaurantCitiesPage(city, 1)
//
//                viewModel.myResponsPage.observe(viewLifecycleOwner, Observer { response ->
//
//                    response.body()?.restaurants?.forEach {
//                        restaurantsToShow.add(it)
//                    }
//
//                })
//
//                if(restaurantsToShow.size == 0) {
//                }else{
//                    Toast.makeText(context,restaurantsToShow.size.toString()+" restaurants listed!",Toast.LENGTH_SHORT).show()
//                    adapter.setData(restaurantsToShow)
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//            }
//        }

        buttonGo.setOnClickListener {

            adapter.setData(arrayListOf<Restaurant>())

            val city = spinnerCity.selectedItem.toString()
            val page = (spinnerPageNr.selectedItem ?: 1) as Int

            val restaurantsToShow = arrayListOf<Restaurant>()

            viewModel.getRestaurantCitiesPage(city, page)

            viewModel.myResponsPage.observe(viewLifecycleOwner, Observer { response ->

                response.body()?.restaurants?.forEach {

                    restaurantsToShow.add(it)

                }

            })


            if(restaurantsToShow.size == 0) {
                Toast.makeText(context,"No restaurants were found in this city!",Toast.LENGTH_SHORT).show()
            }else{
                adapter.setData(restaurantsToShow)
            }
        }

        setUpBottomNav()

        return view
    }

    private fun setUpBottomNav() {
        val navbar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navbar?.visibility = View.VISIBLE
    }
}