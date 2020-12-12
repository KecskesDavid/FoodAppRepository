package com.example.foodproject.util

//constants class for the app
class Constants {

    //these constants are static so that the app can reach them from everywhere before the app is compiled
    companion object{
        const val BASE_URL="http://ratpark-api.imok.space/"
        var states: ArrayList<String> = ArrayList()
        var cities: ArrayList<String> = ArrayList()
        val PREFERENCES = "MyPrefs"
        val nameSP = "nameKey"
        val phoneSP = "phoneKey"
        val emailSP = "emailKey"
        val addressSP = "addressKey"
        val jobSP = "jobKey"
        val idSP = "idKey"
        val passSP = "passKey"
        val photoSP = "photoKey"

    }
}