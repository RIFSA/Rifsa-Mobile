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
import com.example.rifsa_mobile.model.entity.openweatherapi.WeatherDetailResponse
import com.example.rifsa_mobile.model.entity.openweatherapi.request.WeatherRequest
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestFirebaseEntity
import com.example.rifsa_mobile.model.remote.utils.FetchResult
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList
import kotlin.math.round

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel : HomeFragmentViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }

    private var harvestList = ArrayList<HarvestFirebaseEntity>()
    private var diseaseList = ArrayList<DiseaseFirebaseEntity>()

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
            getUserId().observe(viewLifecycleOwner){ token ->
                getHarvestRemote(token)
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
        viewModel.getWeatherByLocation(WeatherRequest(
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

    private fun getHarvestRemote(token : String){
        viewModel.readHarvestResult(token).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    snapshot.children.forEach { child ->
                        child.children.forEach { main ->
                            val data = main.getValue(HarvestFirebaseEntity::class.java)
                            data?.let { harvestList.add(data) }
                            showHarvestList(harvestList)
                        }
                    }
                }else{
                    showStatus(requireContext().getString(R.string.tidak_ada_data))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                showStatus(error.message)
            }
        })
    }

    private fun showHarvestList(data : List<HarvestFirebaseEntity>){
        try {
            binding.barhomeHarvest.visibility = View.GONE
            val adapter = HarvestResultRecyclerViewAdapter(data)
            val recyclerView = binding.rvHomeHarvest
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            adapter.onDetailCallBack(object : HarvestResultRecyclerViewAdapter.OnDetailCallback{
                override fun onDetailCallback(data: HarvestFirebaseEntity) {
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
        viewModel.getUserId().observe(viewLifecycleOwner){ userId ->
            viewModel.readDiseaseResult(userId).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        snapshot.children.forEach { child->
                            child.children.forEach{ main ->
                                val data = main.getValue(DiseaseFirebaseEntity::class.java)
                                data?.let { diseaseList.add(it) }
                                showDiseaseTotal(diseaseList.size)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("homeFragment",error.message.toString())
                }
            })
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