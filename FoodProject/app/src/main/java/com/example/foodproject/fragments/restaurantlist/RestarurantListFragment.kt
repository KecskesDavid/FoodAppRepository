package com.example.foodproject.fragments.restaurantlist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.example.foodproject.util.Constants.Companion.RESTAURANT_KEY
import com.example.foodproject.util.Constants.Companion.cities
import com.example.foodproject.viewmodel.FavoriteRestaurantsViewModel
import com.example.foodproject.viewmodel.RestaurantViewModel
import com.example.foodproject.viewmodel.RetrofitViewModel
import com.example.foodproject.viewmodel.RetrofitViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_restaurant_list.view.*
import java.net.URLEncoder

class RestarurantListFragment : Fragment(),RestaurantAdapter.OnItemClickListener// -> the one created in the adapter to migrate the functionalities to the fragments
{
    private lateinit var retrofitViewModel: RetrofitViewModel //viewmodel for retrofit
    private lateinit var restaurantViewModel: RestaurantViewModel //viewmodel for favorite table
    private lateinit var favoriteRestaurantViewModel: FavoriteRestaurantsViewModel //viewmodel for favorite table

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurant_list, container, false)

        val repository = RetrofitRepository()//api repo
        val viewModelFactory = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(this,viewModelFactory).get(RetrofitViewModel::class.java)
        restaurantViewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        favoriteRestaurantViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(FavoriteRestaurantsViewModel::class.java)



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
        val adapter = RestaurantAdapter(requireContext(),this)
        val recyclerViewRestaurant = view.restaurantRecView
        recyclerViewRestaurant.adapter=adapter
        recyclerViewRestaurant.layoutManager=LinearLayoutManager(requireContext())

        var flag=false

        if(isNetworkAvailable(context))
        {
            //this function is called every time when navigating to this page, sets placeholders in case there are no restaurants to show
            setUpPlaceHolder(view,flag,recyclerViewRestaurant)

            //to set up first navigation
            retrofitViewModel.getRestaurantCitiesPage(cities[0], 1)

            retrofitViewModel.myResponsPage.observe(viewLifecycleOwner, Observer { response -> //somehow, when the query parameters are encoded the encoded string is not what the url is supposed to search, so there is no result in some cases

                flag = response.body()?.restaurants?.size != 0

                //this function should be called for every click and filter
                setUpPlaceHolder(view,flag,recyclerViewRestaurant)

                adapter.setData(response.body()?.restaurants!!)

            })

            buttonGo.setOnClickListener {

                val city = spinnerCity.selectedItem.toString()
                val page = (spinnerPageNr.selectedItem ?: 1) as Int

                //try to use this to convert my queried city, but did not work
                //val encodedstring = URLEncoder.encode(city, "utf-8") //encoding the special characters
                if(isNetworkAvailable(context)){
                    retrofitViewModel.getRestaurantCitiesPage(city, page)
                }
                else{
                    Toast.makeText(context,"You should connect to internet first!",Toast.LENGTH_SHORT).show()
                }

            }
        }
        else{
            Toast.makeText(context,"There is no internet connection!",Toast.LENGTH_SHORT).show()
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

    //delegation
    //overriding this function to implement the functionality of the adapter -> to navigate to other page
    override fun onItemClick(itemView: View, restaurant: Restaurant) {

        val bundle = Bundle() //for storing data of the current restaurant

        bundle.putParcelable(RESTAURANT_KEY, restaurant)

        val detailsFragment = DetailsFragment()
        detailsFragment.arguments = bundle

        //transaction to another fragment
        val transaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, detailsFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }
}