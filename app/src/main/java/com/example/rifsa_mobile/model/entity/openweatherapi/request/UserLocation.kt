package com.example.rifsa_mobile.model.entity.openweatherapi.request

import retrofit2.http.Query

data class UserLocation(
    var location: String? = null,
    var latitude: Double? = null,
    var longtitude: Double? = null,
)
