package com.example.rifsa_mobile.view.fragment.disease.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.databinding.ItemcardDisaseBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity

class DiseaseRecyclerViewAdapter(private var dataList : List<DiseaseEntity>): RecyclerView.Adapter<DiseaseRecyclerViewAdapter.ViewHolder>() {
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
            tvDiseaseSubtitle.text = item.dateDisease
            tvDiseaseTitle.text = item.nameDisease
        }

        holder.itemView.setOnClickListener {
            diseaseDetail.onDetailCallback(item)
        }
    }

    override fun getItemCount(): Int = dataList.size


    interface OnDetailCallback{
        fun onDetailCallback(data : DiseaseEntity)
    }

}