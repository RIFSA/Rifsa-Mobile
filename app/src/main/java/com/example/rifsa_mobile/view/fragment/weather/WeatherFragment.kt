package com.example.rifsa_mobile.view.fragment.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.databinding.FragmentWeatherBinding
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.forecast.ForecastItem
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.view.fragment.weather.adapter.ForecastRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.round

class WeatherFragment : Fragment() {
    private lateinit var binding : FragmentWeatherBinding

    private val viewModel : WeatherFragmentViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(layoutInflater)
        viewModel.getUserLocation().observe(viewLifecycleOwner){ location ->
            lifecycleScope.launch {
                getWeatherData(
                    location.latitude ?: 0.0,
                    location.longtitude ?: 0.0
                )
            }
        }
        return binding.root
    }

    private suspend fun getWeatherData(latitude: Double,longitude: Double){
        viewModel.getWeatherDataByLocation(UserLocation(
            location = null,
            latitude = latitude,
            longtitude = longitude
        )).observe(viewLifecycleOwner){ respon->
            when(respon){
                is FetchResult.Loading->{
                    binding.pgbarWeather.visibility = View.VISIBLE
                }

                is FetchResult.Success->{
                    binding.pgbarWeather.visibility = View.INVISIBLE
                    setWeatherDetail(respon.data)
                }
                is FetchResult.Error->{
                    binding.pgbarWeather.visibility = View.INVISIBLE
                }
                else -> {
                    Log.d(TAG,"no data")
                }
            }
        }

        viewModel.getWeatherForecastData(UserLocation(
            location = null,
            latitude = latitude,
            longtitude = longitude
        )).observe(viewLifecycleOwner){ respon ->
            when(respon){
                is FetchResult.Loading->{
                    binding.pgbarForecast.visibility = View.VISIBLE
                }

                is FetchResult.Success->{
                    binding.pgbarForecast.visibility = View.INVISIBLE
                    setForecast(respon.data.list)
                }
                is FetchResult.Error->{
                    binding.pgbarForecast.visibility = View.INVISIBLE
                }
                else -> {
                    Log.d(TAG,"no data")
                }
            }
        }
    }

    private fun setWeatherDetail(data : WeatherDetailResponse){
        val url = data.weather[0].icon
        val icon = "http://openweathermap.org/img/w/${url}.png"
        Glide.with(requireContext())
            .asBitmap()
            .load(icon)
            .into(binding.imgWeatherIcon)

        val temp = round(data.main.temp).toInt()

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = data.timezone.toLong() * 1000

        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        binding.tvWeatherCity.text = data.name
        binding.tvWeatherTemp.text = "$temp c"
        binding.tvWeatherDate.text = "${day}-${month}-${year}"
        binding.tvWeatherDesc.text = data.weather[0].description

        binding.tvWeatherCloud.text = "${data.clouds.all} %"
        binding.tvWeatherHumid.text = "${data.main.humidity} %"
        binding.tvWeatherWind.text = "${data.wind.speed} km/h"
    }


    private fun setForecast(forecast : List<ForecastItem>){
        val adapter = ForecastRecyclerViewAdapter(forecast)
        binding.recviewForecast.adapter = adapter
        binding.recviewForecast.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object{
        private const val TAG = "weather_fragment"
    }
}