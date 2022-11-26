package com.example.rifsa_mobile.model.entity.openweatherapi

import com.google.gson.annotations.SerializedName

data class WeatherDetailResponse(
 @SerializedName("base")
 var base: String,
 @SerializedName("clouds")
 var clouds: Clouds,
 @SerializedName("cod")
 var cod: Int,
 @SerializedName("coord")
 var coord: Coord,
 @SerializedName("dt")
 var dt: Int,
 @SerializedName("id")
 var id: Int,
 @SerializedName("main")
 var main: Main,
 @SerializedName("name")
 var name: String,
 @SerializedName("sys")
 var sys: Sys,
 @SerializedName("timezone")
 var timezone: Int,
 @SerializedName("visibility")
 var visibility: Int,
 @SerializedName("weather")
 var weather: List<WeatherX>,
 @SerializedName("wind")
 var wind: Wind
)

class Clouds(
 @SerializedName("all")
 var all: Int
)

class Coord(
 @SerializedName("lat")
 var lat: Double,
 @SerializedName("lon")
 var lon: Double
)

class Sys(
 @SerializedName("country")
 var country: String,
 @SerializedName("id")
 var id: Int,
 @SerializedName("sunrise")
 var sunrise: Int,
 @SerializedName("sunset")
 var sunset: Int,
 @SerializedName("type")
 var type: Int
)

class Main(
 @SerializedName("feels_like")
 var feelsLike: Double,
 @SerializedName("humidity")
 var humidity: Int,
 @SerializedName("pressure")
 var pressure: Int,
 @SerializedName("temp")
 var temp: Double,
 @SerializedName("temp_max")
 var tempMax: Double,
 @SerializedName("temp_min")
 var tempMin: Double
)

class WeatherX(
 @SerializedName("description")
 var description: String,
 @SerializedName("icon")
 var icon: String,
 @SerializedName("id")
 var id: Int,
 @SerializedName("main")
 var main: String
)

class Wind(
 @SerializedName("deg")
 var deg: Int,
 @SerializedName("speed")
 var speed: Double
)