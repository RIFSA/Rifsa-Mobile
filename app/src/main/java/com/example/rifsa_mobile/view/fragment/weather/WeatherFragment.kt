package com.example.rifsa_mobile.view.fragment.weather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentWeatherBinding
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.launch
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
        lifecycleScope.launch {
            getWeatherDataBySearch("london")
        }
        return binding.root
    }

    private suspend fun getWeatherDataBySearch(location : String){
        viewModel.getWeatherDataBySearch(location).observe(viewLifecycleOwner){ respon->
            when(respon){
                is FetchResult.Loading->{

                }
                is FetchResult.Success->{

                    setWeatherDetail(respon.data)
                }
                is FetchResult.Error->{

                }
                else -> {
                    Log.d(TAG,"no data")
                }
            }
        }
    }

    private fun setWeatherDetail(data : WeatherDetailResponse){
        binding.tvWeatherCity.text = "london"

        val url = data.weather[0].icon
        val icon = "http://openweathermap.org/img/w/${url}.png"
        Glide.with(requireContext())
            .asBitmap()
            .load(icon)
            .into(binding.imgWeatherIcon)

        val temp = round(data.main.temp).toInt()
        binding.tvWeatherTemp.text = "$temp c"

        binding.tvWeatherCloud.text = "${data.clouds.all} %"
        binding.tvWeatherHumid.text = "${data.main.humidity} %"

    }
    companion object{
        private const val TAG = "weather_fragment"
    }
}