package com.example.foodproject.adapters

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodproject.R
import com.example.foodproject.data.FavoriteRestaurants
import com.example.foodproject.fragments.details.DetailsFragment
import com.example.foodproject.data.Restaurant
import com.example.foodproject.util.Constants
import com.example.foodproject.util.Constants.Companion.idSP
import com.example.foodproject.util.Constants.Companion.nameSP
import com.example.foodproject.viewmodel.FavoriteRestaurantsViewModel
import com.example.foodproject.viewmodel.RestaurantViewModel
import kotlinx.android.synthetic.main.restaurant_list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RestaurantAdapter(val context: Context): RecyclerView.Adapter<RestaurantAdapter.RestaurantAdapterHolder>(){

    private var list = emptyList<Restaurant>()
    private var mFavoriteRestaurants: FavoriteRestaurantsViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(FavoriteRestaurantsViewModel::class.java)
    private var mRestaurants: RestaurantViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(RestaurantViewModel::class.java)
    private var mFavoritesRestaurantsViewModel: FavoriteRestaurantsViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(FavoriteRestaurantsViewModel::class.java)
    private val sharedPreferences = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)

    companion object {
        const val IMAGE_VIEW = "imageView"
        const val NAME_TXT = "nameTxt"
        const val ADRESS_TXT = "adressTxt"
        const val STATE_TXT = "state"
        const val CITY_TXT = "city"
        const val PRICE_TXT = "priceTxt"
        const val LNG_TXT = "lngTxt"
        const val LAT_TXT = "latTxt"
        const val TELL_NR_TXT = "tellNrTxt"
        const val RESERVE_URL = "reserve_url"
    }

    class RestaurantAdapterHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val imageView: ImageView = itemView.imageView
        val nameTxt: TextView = itemView.nameTxt
        val adressTxt: TextView = itemView.adressTxt
        val tellNrTxt: TextView = itemView.telNumberTxt
        val addToFav: ImageView = itemView.addToFav
        val remove: ImageView = itemView.removeBtn
    }

    //only once called
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantAdapterHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_list_item, parent, false)

        return RestaurantAdapterHolder(itemView)
    }

    //multiple times
    override fun onBindViewHolder(holder: RestaurantAdapterHolder, position: Int) {
        val isActive = sharedPreferences.getString(nameSP,"")
        val currentItem = list[position]

        if (isActive?.isEmpty() == false) {
            holder.addToFav.visibility = View.VISIBLE

            holder.remove.setOnClickListener{
                val builder = AlertDialog.Builder(context)
                builder.setPositiveButton("Yes"){_,_ ->
                    //mFavoriteRestaurants.deleteRestaurant(currentItem)
                    mFavoriteRestaurants.readAllFavoriteRestaurants.observe(context as LifecycleOwner, Observer {
                        it.forEach {
                            if (it.user_id == sharedPreferences.getInt(idSP, 0) && it.restaurant_id == currentItem.id) {
                                mFavoriteRestaurants.deleteFavorites(it)
                            }
                        }
                        Toast.makeText(context, "Succesfully deleted: " + currentItem.name, Toast.LENGTH_SHORT).show()
                    })
                }
                builder.setNegativeButton("No"){_,_->}
                builder.setMessage("Are you sure you want to delete ${currentItem.name}?")
                builder.create().show()

                return@setOnClickListener
            }

            holder.imageView.setOnClickListener{
                //todo
            }
        }
        else
        {
            holder.addToFav.visibility = View.GONE
        }

        GlobalScope.launch(Dispatchers.Main) {
           Glide.with(context)
                   .load(currentItem.image_url)
                   .into(holder.imageView)
        }
        holder.nameTxt.text=currentItem.name
        holder.tellNrTxt.text=currentItem.phone
        holder.adressTxt.text=currentItem.address

        mFavoritesRestaurantsViewModel.readAllFavoriteRestaurants.observe(context as LifecycleOwner, Observer { resp ->

            if (isActive?.isEmpty() == false) {

                resp.forEach {
                    if(it.user_id == sharedPreferences?.getInt(idSP,0))
                    {
                        if( currentItem.id == it.restaurant_id)
                        {
                            holder.remove.visibility = View.VISIBLE
                            holder.addToFav.setImageResource(R.drawable.ic_favorite)
                        }
                    }
                }

            }

        })


        holder.itemView.setOnClickListener {

            val bundle = Bundle()

            bundle.putString(IMAGE_VIEW, currentItem.image_url)
            bundle.putString(NAME_TXT, currentItem.name)
            bundle.putString(ADRESS_TXT, currentItem.address)
            bundle.putString(LAT_TXT, currentItem.lat.toString())
            bundle.putString(LNG_TXT, currentItem.lng.toString())
            bundle.putString(TELL_NR_TXT, currentItem.phone)
            bundle.putString(CITY_TXT, currentItem.city)
            bundle.putString(STATE_TXT, currentItem.state)
            bundle.putString(RESERVE_URL, currentItem.reserve_url)
            bundle.putString(PRICE_TXT, currentItem.price.toString())

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

            mFavoriteRestaurants.addFavoriteRestaurants(FavoriteRestaurants(0,currentItem.id,sharedPreferences.getInt(idSP,0)))
            mRestaurants.addRestaurant(currentItem)
        }

    }

    override fun getItemCount() = list.size

    fun setData(restaurant: List<Restaurant>){
        this.list = restaurant
        notifyDataSetChanged()
    }

}