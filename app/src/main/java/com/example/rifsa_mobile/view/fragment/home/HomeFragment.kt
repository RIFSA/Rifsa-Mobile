package com.example.rifsa_mobile.view.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHomeBinding
import com.example.rifsa_mobile.helpers.utils.Utils.internetChecker
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.request.UserLocation
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.math.round

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel : HomeFragmentViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    private var harvestList = ArrayList<HarvestEntity>()
    private var diseaseList = ArrayList<DiseaseEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.mainHomeLayout.fullScroll(ScrollView.FOCUS_UP)

        binding.imageView2.setImageResource(R.drawable.mockprofile)
        diseaseCount()

        viewModel.apply {
            getUserName().observe(viewLifecycleOwner){ name ->
                binding.tvhomeName.text = name
            }
        }

        viewModel.getUserLocation().observe(viewLifecycleOwner){ location->
            lifecycleScope.launch {
                getWeatherData(
                    location.latitude ?: 0.0,
                    location.longtitude ?: 0.0
                )
            }
        }

        if (internetChecker(requireContext())){
            binding.cardWeather.visibility = View.VISIBLE
        }

        readHarvestData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardWeather.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToWeatherFragment()
            )
        }

        binding.btnHomeHasil.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToHarvetResultFragment()
            )
        }
    }


    private suspend fun getWeatherData(latitude : Double,longitude: Double){
        viewModel.getWeatherByLocation(UserLocation(
                null,
                latitude = latitude,
                longtitude = longitude
            )).observe(viewLifecycleOwner){ respon ->
            when(respon){
                is FetchResult.Loading->{
                    binding.pgbarWeatherHome.visibility = View.VISIBLE
                }
                is FetchResult.Success->{
                    binding.pgbarWeatherHome.visibility = View.GONE
                    showWeather(respon.data)
                }
                is FetchResult.Error->{
                    binding.pgbarWeatherHome.visibility = View.GONE
                    Log.d("homeFragment",respon.error)
                }
                else -> {}
            }
        }
    }

    private fun readHarvestData(){
        lifecycleScope.launch {
            try {
                viewModel.readHarvestLocal().observe(viewLifecycleOwner){ data ->
                    showHarvestList(data)
                }
            }catch ( e : Exception){
                Log.d("homefragment",e.toString())
            }
        }

    }

    private fun showHarvestList(data : List<HarvestEntity>){
        try {
            binding.barhomeHarvest.visibility = View.GONE
            val adapter = HarvestResultRecyclerViewAdapter(data)
            val recyclerView = binding.rvHomeHarvest
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter.onDetailCallBack(object : HarvestResultRecyclerViewAdapter.OnDetailCallback{
                override fun onDetailCallback(data: HarvestEntity) {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHarvestInsertDetailFragment(data))
                }
            })
        }catch (e : Exception){
            Log.d("HomeFragment",e.message.toString())
        }
    }

    private fun showWeather(data : WeatherDetailResponse){
        val temp = round(data.main.temp).toInt()
        val url = data.weather[0].icon

        val icon = "http://openweathermap.org/img/w/${url}.png"
        binding.tvCityName.text = data.name
        binding.tvWeatherhomeDesc.text = data.weather[0].description
        binding.tvWeatherhomeTemp.text = "$temp c"
        Glide.with(requireContext())
            .asBitmap()
            .load(icon)
            .into(binding.imgWeatherhomeIcon)
    }


    private fun diseaseCount(){
        lifecycleScope.launch {
            viewModel.readDiseaseLocal().observe(viewLifecycleOwner){ data ->
                showDiseaseTotal(data.size)
            }
        }
    }

    private fun showDiseaseTotal(total : Int){
        if(total != 0){
            binding.cardViewTwo.visibility = View.VISIBLE
            binding.tvhomeDisasecount.text = total.toString()
        }
    }

    private fun showStatus(title : String){
        binding.apply {
            cardViewOne.visibility = View.VISIBLE
            tvhomeHarvestStatus.visibility = View.VISIBLE
            barhomeHarvest.visibility = View.GONE
            barhomeHarvest.visibility = View.GONE
            tvhomeHarvestStatus.text = title
            Log.d("HomeFragment", title)
        }
    }
}