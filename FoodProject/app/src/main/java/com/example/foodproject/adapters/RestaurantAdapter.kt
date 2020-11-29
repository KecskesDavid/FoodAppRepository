package com.example.foodproject.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.foodproject.R
import com.example.foodproject.model.Restaurant
import kotlinx.android.synthetic.main.restaurant_list_item.view.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.foodproject.MainActivity
import com.example.foodproject.fragments.DetailsFragment


class RestaurantAdapter(val context: Context): RecyclerView.Adapter<RestaurantAdapter.RestaurantAdapterHolder>(){

    private var list = emptyList<Restaurant>()

    class RestaurantAdapterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.imageView
        val nameTxt: TextView = itemView.nameTxt
        val adressTxt: TextView = itemView.adressTxt
        val tellNrTxt: TextView = itemView.telNumberTxt
        val addToFav: ImageView = itemView.addToFav

    }

    //only once called
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantAdapterHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item,parent,false)

        return RestaurantAdapterHolder(itemView)
    }

    //multiple times
    override fun onBindViewHolder(holder: RestaurantAdapterHolder, position: Int) {
        val currentItem = list[position]

        //todo
        //holder.imageView.setImageResource(currentItem.image_url)
        holder.nameTxt.text=currentItem.name
        holder.tellNrTxt.text=currentItem.phone
        holder.adressTxt.text=currentItem.address

        holder.itemView.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(IMAGE_VIEW, currentItem.image_url)
            bundle.putString(NAME_TXT, currentItem.name)
            bundle.putString(ADRESS_TXT, currentItem.address)
            bundle.putString(LAT_TXT, currentItem.lat.toString())
            bundle.putString(LNG_TXT, currentItem.lng.toString())
            bundle.putString(TELL_NR_TXT, currentItem.phone)
            bundle.putString(RESERVE_URL, currentItem.reserve_url)

            val detailsFragment = DetailsFragment()
            detailsFragment.arguments = bundle

            val transaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, detailsFragment)
            transaction.addToBackStack(null);
            transaction.commit()
        }

    }

    override fun getItemCount() = list.size

    fun setData(restaurant: List<Restaurant>){
        this.list = restaurant
        notifyDataSetChanged()
    }

    companion object {
        const val IMAGE_VIEW = "ImageView"
        const val NAME_TXT = "nameTxt"
        const val ADRESS_TXT = "adressTxt"
        const val LNG_TXT = "lngTxt"
        const val LAT_TXT = "latTxt"
        const val TELL_NR_TXT = "tellNrTxt"
        const val RESERVE_URL = "reserve_url"
    }
}