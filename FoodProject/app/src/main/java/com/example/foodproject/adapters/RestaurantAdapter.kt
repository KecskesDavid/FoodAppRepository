package com.example.foodproject.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.foodproject.MainActivity
import com.example.foodproject.R
import com.example.foodproject.data.FavoriteRestaurants
import com.example.foodproject.fragments.DetailsFragment
import com.example.foodproject.data.Restaurant
import com.example.foodproject.viewmodel.FavoriteRestaurantsViewModel
import com.example.foodproject.viewmodel.RestaurantViewModel
import kotlinx.android.synthetic.main.restaurant_list_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class RestaurantAdapter(val context: Context): RecyclerView.Adapter<RestaurantAdapter.RestaurantAdapterHolder>(){

    private var list = emptyList<Restaurant>()
    private var mFavoriteRestaurants: FavoriteRestaurantsViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(FavoriteRestaurantsViewModel::class.java)
    private var mRestaurants: RestaurantViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(RestaurantViewModel::class.java)

    class RestaurantAdapterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.imageView
        val nameTxt: TextView = itemView.nameTxt
        val adressTxt: TextView = itemView.adressTxt
        val tellNrTxt: TextView = itemView.telNumberTxt
        val addToFav: ImageView = itemView.addToFav

    }

    //only once called
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantAdapterHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item, parent, false)

        return RestaurantAdapterHolder(itemView)
    }

    //multiple times
    override fun onBindViewHolder(holder: RestaurantAdapterHolder, position: Int) {
        val currentItem = list[position]

        //not very effective
        GlobalScope.launch {
            val bitmap = getBitmapFromURL(currentItem.image_url) !!
            (context as MainActivity).runOnUiThread { holder.imageView.setImageBitmap(bitmap) }
        }
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

        holder.addToFav.setOnClickListener{
            holder.addToFav.setImageResource(R.drawable.ic_favorite)
            Toast.makeText(context,"Restaurant added to favorites!",Toast.LENGTH_SHORT).show()

            mFavoriteRestaurants.addFavoriteRestaurants(FavoriteRestaurants(0,currentItem.id,1))
            mRestaurants.addRestaurant(currentItem)
        }

        //todo place it to fragment
        holder.itemView.setOnLongClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setPositiveButton("Yes"){_,_ ->
                mFavoriteRestaurants.deleteRestaurant(currentItem)
                Toast.makeText(context,"Succesfully deleted: "+currentItem.name,Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton("No"){_,_->}
            builder.setMessage("Are you sure you want to delete ${currentItem.name}?")
            builder.create().show()
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount() = list.size

    fun setData(restaurant: List<Restaurant>){
        this.list = restaurant
        notifyDataSetChanged()
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url
                    .openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
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