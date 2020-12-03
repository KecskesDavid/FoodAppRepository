package com.example.foodproject.util

//constants class for the app
class Constants {

    //these constants are static so that the app can reach them from everywhere before the app is compiled
    companion object{
        const val BASE_URL="http://opentable.herokuapp.com"
        lateinit var states: ArrayList<String>
        lateinit var cities: ArrayList<String>
        val PREFERENCES = "MyPrefs"
        val nameSP = "nameKey"
        val phoneSP = "phoneKey"
        val emailSP = "emailKey"
        val addressSP = "addressKey"
        val idSP = "addressId"

    }
}