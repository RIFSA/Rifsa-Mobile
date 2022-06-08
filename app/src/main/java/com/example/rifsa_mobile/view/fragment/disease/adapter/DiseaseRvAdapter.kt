package com.example.rifsa_mobile.view.fragment.disease.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ItemcardDisaseBinding
import com.example.rifsa_mobile.model.entity.remote.disease.DiseaseResultDataResponse
import com.example.rifsa_mobile.utils.Utils

class DiseaseRvAdapter(private var dataList : List<DiseaseResultDataResponse>): RecyclerView.Adapter<DiseaseRvAdapter.ViewHolder>() {
    class ViewHolder(var binding : ItemcardDisaseBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var diseaseDetail : OnDetailCallback

    fun onDiseaseDetailCallback(callback : OnDetailCallback){
        this.diseaseDetail = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardDisaseBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.apply {
            tvDiseaseSubtitle.text = item.createdAt.removeRange(10..18)
            tvDiseaseTitle.text = item.indikasi


            holder.binding.imgIcondisease.setImageResource(R.drawable.ic_warning)
        }

        holder.itemView.setOnClickListener {
            diseaseDetail.onDetailCallback(item)
        }
    }

    override fun getItemCount(): Int = dataList.size


    interface OnDetailCallback{
        fun onDetailCallback(data : DiseaseResultDataResponse)
    }

}