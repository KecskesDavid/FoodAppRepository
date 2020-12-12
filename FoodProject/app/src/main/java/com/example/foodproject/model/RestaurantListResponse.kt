package com.example.foodproject.model

class RestaurantListResponse(
        val total_entries: Int,
        val page: Int,
        val per_page: Int,
        val restaurants: List<Restaurant>
) {
}