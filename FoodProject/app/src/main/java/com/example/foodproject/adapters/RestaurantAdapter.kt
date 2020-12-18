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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodproject.R
import com.example.foodproject.model.FavoriteRestaurants
import com.example.foodproject.fragments.details.DetailsFragment
import com.example.foodproject.model.Restaurant
import com.example.foodproject.util.Constants
import com.example.foodproject.util.Constants.Companion.RESTAURANT_KEY
import com.example.foodproject.util.Constants.Companion.idSP
import com.example.foodproject.util.Constants.Companion.nameSP
import com.example.foodproject.viewmodel.FavoriteRestaurantsViewModel
import com.example.foodproject.viewmodel.RestaurantViewModel
import kotlinx.android.synthetic.main.restaurant_list_item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll

class RestaurantAdapter(val context: Context): RecyclerView.Adapter<RestaurantAdapter.RestaurantAdapterHolder>()/*, ActionMode.Callback*/
{
    private var listOfRestaurants = emptyList<Restaurant>() // list for restaurants
    private var mRestaurantsViewModel: RestaurantViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(RestaurantViewModel::class.java) //viewmodel for restaurants
    private var mFavoritesRestaurantsViewModel: FavoriteRestaurantsViewModel = ViewModelProvider(context as ViewModelStoreOwner).get(FavoriteRestaurantsViewModel::class.java) //viewmodel for favorite table
    private val sharedPreferences = context.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE) //for login system

    class RestaurantAdapterHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        //binding ui element with value
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


    //multiple times, every time filling the data to the created adapter
    override fun onBindViewHolder(holder: RestaurantAdapterHolder, position: Int) {
        val currentItem = listOfRestaurants[position]
        //binding data with view/adapter
        GlobalScope.launch(Dispatchers.Main) { //using glide to load image from url
            Glide.with(context)
                    .load(currentItem.image_url)
                    .into(holder.imageView)
        }
        holder.nameTxt.text=currentItem.name
        holder.tellNrTxt.text=currentItem.phone
        holder.adressTxt.text=currentItem.address


        /*--- functionalities ---*/
        val isActive = sharedPreferences.getString(nameSP,"") //taking the name field of the shared pref to check if the user is loged or not

        if (isActive?.isEmpty() == false) {
            holder.addToFav.visibility = View.VISIBLE //changing adapter view

            //event to remove an item from favorites
            holder.remove.setOnClickListener{
                val builder = AlertDialog.Builder(context)
                builder.setPositiveButton("Yes"){_,_ ->

                    deleteRestaurantFromDatabase(mFavoritesRestaurantsViewModel,currentItem)

                }
                builder.setNegativeButton("No"){_,_->}
                builder.setMessage("Are you sure you want to delete ${currentItem.name}?")
                builder.create().show()

                return@setOnClickListener
            }

            //changing ui of adapter
            mFavoritesRestaurantsViewModel.readAllFavoriteRestaurants.observe(context as LifecycleOwner, Observer { resp ->
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

            })

        }
        else
        {
            holder.addToFav.visibility = View.GONE
        }


        //opens details fragment
        holder.itemView.setOnClickListener {

            val bundle = Bundle() //for storing data of the current restaurant

            bundle.putParcelable(RESTAURANT_KEY, currentItem)

            val detailsFragment = DetailsFragment()
            detailsFragment.arguments = bundle

            //transaction to another fragment
            val transaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host_fragment, detailsFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //add restaurant to favorites
        holder.addToFav.setOnClickListener{
            Toast.makeText(context,"Restaurant added to favorites!",Toast.LENGTH_SHORT).show()

            mFavoritesRestaurantsViewModel.addFavoriteRestaurants(FavoriteRestaurants(0,currentItem.id,sharedPreferences.getInt(idSP,0)))
            mRestaurantsViewModel.addRestaurant(currentItem)
        }

    }

    override fun getItemCount() = listOfRestaurants.size

    fun setData(restaurant: List<Restaurant>){
        this.listOfRestaurants = restaurant
        //to refresh the recycler view
        notifyDataSetChanged()
    }

    private fun deleteRestaurantFromDatabase(mFavoritesRestaurantsViewModel: FavoriteRestaurantsViewModel, currentItem: Restaurant) {
        mFavoritesRestaurantsViewModel.readAllFavoriteRestaurants.observe(context as LifecycleOwner, Observer {
            it.forEach {
                if (it.user_id == sharedPreferences.getInt(idSP, 0) && it.restaurant_id == currentItem.id) {
                    //delete only the connection between user and restaurant, the restaurant remains in the db for other users
                    mFavoritesRestaurantsViewModel.deleteFavorites(it)//todo at delete the list should be refreshed
                }
            }
        })
    }

}