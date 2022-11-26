package com.example.rifsa_mobile.view.fragment.weather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.launch

class WeatherFragment : Fragment() {
    private val viewModel : WeatherFragmentViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        lifecycleScope.launch {
            getWeatherDataBySearch("london")
        }
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    private suspend fun getWeatherDataBySearch(location : String){
        viewModel.getWeatherDataBySearch(location).observe(viewLifecycleOwner){ respon->
            when(respon){
                is FetchResult.Loading->{

                }
                is FetchResult.Success->{
                    Log.d(TAG,respon.data.weather[0].description)
                }
                is FetchResult.Error->{

                }
                else -> {
                    Log.d(TAG,"no data")
                }
            }
        }
    }

    companion object{
        private const val TAG = "weather_fragment"
    }
}