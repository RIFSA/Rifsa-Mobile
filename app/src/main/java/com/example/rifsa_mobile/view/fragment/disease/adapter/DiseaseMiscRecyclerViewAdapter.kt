package com.example.rifsa_mobile.view.fragment.disease.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.databinding.ItemcardDiseaseMiscBinding

class DiseaseMiscRecyclerViewAdapter(private var dataList: List<String>): RecyclerView.Adapter<DiseaseMiscRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(var binding : ItemcardDiseaseMiscBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(ItemcardDiseaseMiscBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.apply {
            tvDiseaseTreatmentDetail.text = item
        }
    }

    override fun getItemCount(): Int = dataList.size
}