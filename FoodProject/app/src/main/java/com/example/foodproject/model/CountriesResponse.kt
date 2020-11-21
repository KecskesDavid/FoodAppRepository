package com.example.foodproject.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountriesResponse(
        val count: Int,
        val countries: List<String>
) {

}