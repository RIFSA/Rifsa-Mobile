package com.example.rifsa_mobile.view.fragment.weather.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.databinding.FragmentWeatherBinding
import com.example.rifsa_mobile.databinding.ItemcardForecastBinding
import com.example.rifsa_mobile.model.entity.openweatherapi.forecast.ForecastItem
import java.util.*
import kotlin.math.round

class ForecastRecyclerViewAdapter(private var forecastItem : List<ForecastItem>)
    : RecyclerView.Adapter<ForecastRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder( val binding: ItemcardForecastBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemcardForecastBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = forecastItem[position]
        holder.binding.apply {
            val url = item.weather[0].icon
            val icon = "http://openweathermap.org/img/w/${url}.png"
            val temp = round(item.main.temp).toInt()
            Glide.with(holder.itemView.context)
                .asBitmap()
                .load(icon)
                .into(imgForecastIcon)

            tvForecastDesc.text = item.weather[0].description
            tvForecastDate.text = item.dtTxt
            tvForecastTemp.text = "${temp} C"
        }
    }

    override fun getItemCount(): Int = forecastItem.size


}