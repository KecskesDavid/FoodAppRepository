package com.example.foodproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodproject.R
import com.example.foodproject.model.Restaurant
import kotlinx.android.synthetic.main.restaurant_list_item.view.*

class RestaurantAdapter: RecyclerView.Adapter<RestaurantAdapter.RestaurantAdapterHolder>(){

    private var list = emptyList<Restaurant>()

    class RestaurantAdapterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.imageView
        val nameTxt: TextView = itemView.nameTxt
        val adressTxt: TextView = itemView.adressTxt
        val tellNrTxt: TextView = itemView.telNumberTxt
    }

    //only once called
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantAdapterHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item,parent,false)

        return RestaurantAdapterHolder(itemView)
    }

    //multiple times
    override fun onBindViewHolder(holder: RestaurantAdapterHolder, position: Int) {
        val currentItem = list[position]

        //holder.imageView.setImageResource(currentItem.image_url)
        holder.nameTxt.text=currentItem.name
        holder.tellNrTxt.text=currentItem.phone
        holder.adressTxt.text=currentItem.address

    }

    override fun getItemCount() = list.size

    fun setData(restaurant: List<Restaurant>){
        this.list = restaurant
        notifyDataSetChanged()
    }
}