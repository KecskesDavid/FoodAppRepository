package com.example.foodproject.fragments.details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodproject.R
import com.example.foodproject.model.Restaurant
import com.example.foodproject.repository.RetrofitRepository
import com.example.foodproject.util.Constants.Companion.RESTAURANT_KEY
import com.example.foodproject.viewmodel.RetrofitViewModel
import com.example.foodproject.viewmodel.RetrofitViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private lateinit var retrofitViewModel: RetrofitViewModel //viewmodel for retrofit

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        val repository = RetrofitRepository() //api repo
        val viewModelFactory = RetrofitViewModelFactory(repository)
        retrofitViewModel = ViewModelProvider(this, viewModelFactory).get(RetrofitViewModel::class.java)

        //Getting the data from the adapter, one restaurant item
        val restaurantToShow : Restaurant = requireArguments().getParcelable(RESTAURANT_KEY) !!

        //Binding the data with the ui elements
        val profile_image = view.findViewById<ImageView>(R.id.profile_ImgView)
        val statecity_txt = view.findViewById<TextView>(R.id.statecity)
        val name_txt = view.findViewById<TextView>(R.id.name_Txt)
        val address_txt = view.findViewById<TextView>(R.id.address_txt)
        val tell_txt = view.findViewById<TextView>(R.id.tell_txt)
        val price_txt = view.findViewById<TextView>(R.id.price_txt)
        GlobalScope.launch(Dispatchers.Main) {
            Glide.with(requireContext())
                    .load(restaurantToShow.image_url)
                    .into(profile_image)
        }
        name_txt.text = restaurantToShow.name
        address_txt.text = restaurantToShow.address
        tell_txt.text = restaurantToShow.phone
        price_txt.text = restaurantToShow.price.toString()
        statecity_txt.text = restaurantToShow.city + ", " + restaurantToShow.state

        //Setting up buttons for map and for searching the restaurant on the internet
        val url_Btn = view.findViewById<ImageView>(R.id.url_Btn)
        val gps_Btn = view.findViewById<ImageView>(R.id.gps_Btn)


        /*--- Functionalities ----*/

        //listener to phone number -> opens phone
        tell_txt.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${restaurantToShow.phone}")
            startActivity(intent)
        }

        //listener to google maps -> opens google maps and locates restaurant
        gps_Btn.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:${restaurantToShow.lat},${restaurantToShow.lng}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        //listener for restaurant web page -> opens google and the restaurants web page
        url_Btn.setOnClickListener {
            var reserve_url=restaurantToShow.reserve_url

            //If the string doesn't start with http://
            if (!reserve_url.startsWith("http://") && !reserve_url.startsWith("https://"))
                reserve_url = "http://" + reserve_url

            val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(reserve_url))
            startActivity(intent)
        }

        //making bottom nav gone
        setUpBottomNav()

        return view
    }


    private fun setUpBottomNav() {
        val navbar = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        navbar?.visibility = View.GONE
    }

}