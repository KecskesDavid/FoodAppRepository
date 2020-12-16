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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodproject.R
import com.example.foodproject.adapters.RestaurantAdapter.Companion.ADRESS_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.CITY_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.IMAGE_VIEW
import com.example.foodproject.adapters.RestaurantAdapter.Companion.LAT_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.LNG_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.NAME_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.PRICE_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.RESERVE_URL
import com.example.foodproject.adapters.RestaurantAdapter.Companion.REST_ID
import com.example.foodproject.adapters.RestaurantAdapter.Companion.STATE_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.TELL_NR_TXT
import com.example.foodproject.repository.RetrofitRepository
import com.example.foodproject.viewmodel.RestaurantViewModel
import com.example.foodproject.viewmodel.RetrofitViewModel
import com.example.foodproject.viewmodel.RetrofitViewModelFactory
import com.example.foodproject.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailsFragment : Fragment() {

    private lateinit var viewModel: RetrofitViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        val repository = RetrofitRepository() //api repo
        val viewModelFactory = RetrofitViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RetrofitViewModel::class.java)
        runBlocking { viewModel.getRestaurant(id) }

        //Getting the data from the adapter
        val id = requireArguments().getInt(REST_ID)
        val image = requireArguments().getString(IMAGE_VIEW)
        val name = requireArguments().getString(NAME_TXT)
        val address = requireArguments().getString(ADRESS_TXT)
        val price = requireArguments().getString(PRICE_TXT)
        val lat = requireArguments().getString(LAT_TXT)
        val lng = requireArguments().getString(LNG_TXT)
        val phone = requireArguments().getString(TELL_NR_TXT)
        val city = requireArguments().getString(STATE_TXT)
        val state = requireArguments().getString(CITY_TXT)
        var reserve_url = requireArguments().get(RESERVE_URL).toString()

        //Binding the data with the ui elements
        val image_view = view.findViewById<ImageView>(R.id.profile_ImgView)
        val statecity_txt = view.findViewById<TextView>(R.id.statecity)
        val name_txt = view.findViewById<TextView>(R.id.name_Txt)
        val address_txt = view.findViewById<TextView>(R.id.address_txt)
        val tell_txt = view.findViewById<TextView>(R.id.tell_txt)
        val price_txt = view.findViewById<TextView>(R.id.price_txt)
        GlobalScope.launch(Dispatchers.Main) {
            Glide.with(requireContext())
                    .load(image)
                    .into(image_view)
        }
        viewModel.myRespons.observe(viewLifecycleOwner, Observer {
            name_txt.text = it?.body()?.name
            address_txt.text = it?.body()?.address
            tell_txt.text = it?.body()?.phone
            price_txt.text = it?.body()?.price?.toString()
            statecity_txt.text = city + ", " + state
        })
//        name_txt.text = name
//        address_txt.text = address
//        tell_txt.text = phone
//        price_txt.text = price.toString()
//        statecity_txt.text = city + ", " + state


        //Setting up buttons for map and for searching the restaurant on the internet
        val url_Btn = view.findViewById<ImageView>(R.id.url_Btn)
        val gps_Btn = view.findViewById<ImageView>(R.id.gps_Btn)


        //listener to phone number -> opens phone
        tell_txt.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phone")
            startActivity(intent)
        }

        //listener to google maps -> opens google maps and locates restaurant
        gps_Btn.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:$lat,$lng")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        //listener for restaurant web page -> opens google and the restaurants web page
        url_Btn.setOnClickListener {
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