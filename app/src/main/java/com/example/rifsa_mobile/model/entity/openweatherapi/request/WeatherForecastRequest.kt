package com.example.rifsa_mobile.model.entity.openweatherapi.request

data class WeatherForecastRequest(
    var location: String? = null,
    var latitude: Double? = null,
    var longtitude: Double? = null,
)