package com.example.rifsa_mobile.view.activity.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.databinding.ItemcardOnboardingBinding
import com.example.rifsa_mobile.model.entity.local.onboarding.OnBoardingPreference

class OnBoardAdapter(var data : List<OnBoardingPreference>) : RecyclerView.Adapter<OnBoardAdapter.ViewHolder>() {
    class ViewHolder(var binding : ItemcardOnboardingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardOnboardingBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.binding.TvBoardPic.setImageResource(item.pic)
        holder.binding.TvBoardTitle.text = item.title
        holder.binding.tvBoardSubtitle.text = item.subTitle

    }

    override fun getItemCount(): Int = data.size
}