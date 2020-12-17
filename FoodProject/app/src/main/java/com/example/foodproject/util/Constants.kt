package com.example.foodproject.util

//constants class for the app
class Constants {

    //these constants are static so that the app can reach them from everywhere before the app is compiled
    companion object{
        const val BASE_URL="http://ratpark-api.imok.space/"
        var states: ArrayList<String> = ArrayList()
        var cities: ArrayList<String> = ArrayList()
        const val RESTAURANT_KEY = "resaurant_key"
        const val PREFERENCES = "MyPrefs"
        const val nameSP = "nameKey"
        const val phoneSP = "phoneKey"
        const val emailSP = "emailKey"
        const val addressSP = "addressKey"
        const val jobSP = "jobKey"
        const val idSP = "idKey"
        const val passSP = "passKey"
        const val photoSP = "photoKey"

    }
}