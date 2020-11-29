package com.example.foodproject.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.foodproject.R
import com.example.foodproject.adapters.RestaurantAdapter.Companion.ADRESS_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.IMAGE_VIEW
import com.example.foodproject.adapters.RestaurantAdapter.Companion.LAT_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.LNG_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.NAME_TXT
import com.example.foodproject.adapters.RestaurantAdapter.Companion.RESERVE_URL
import com.example.foodproject.adapters.RestaurantAdapter.Companion.TELL_NR_TXT

class DetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        //Getting the data from the adapter
        val image = requireArguments().get(IMAGE_VIEW)
        val name = requireArguments().get(NAME_TXT)
        val adress = requireArguments().get(ADRESS_TXT)
        val lat = requireArguments().get(LAT_TXT)
        val lng = requireArguments().get(LNG_TXT)
        val tell = requireArguments().get(TELL_NR_TXT)
        var reserve_url = requireArguments().get(RESERVE_URL).toString()

        //Binding the data with the ui elements
        val image_view = view.findViewById<ImageView>(R.id.profile_ImgView)
        val name_txt = view.findViewById<TextView>(R.id.name_Txt)
        val address_txt = view.findViewById<TextView>(R.id.address_txt)
        val tell_txt = view.findViewById<TextView>(R.id.tell_txt)
        //image_view.text=image
        name_txt.text=name.toString()
        address_txt.text=adress.toString()
        tell_txt.text=tell.toString()

        //Setting up buttons for map and for searching the restaurant on the internet
        val url_Btn = view.findViewById<Button>(R.id.url_Btn)
        val gps_Btn = view.findViewById<Button>(R.id.gps_Btn)

        gps_Btn.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:$lat,$lng")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        url_Btn.setOnClickListener {
            //If the string doesn't start with http://
            if (!reserve_url.startsWith("http://") && !reserve_url.startsWith("https://"))
                reserve_url = "http://" + reserve_url;

            val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(reserve_url))
            startActivity(intent)
        }

        return view
    }

}