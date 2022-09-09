package com.example.rifsa_mobile.view.fragment.diseasewiki.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.databinding.ItemcardDiseaseBookBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseDetailFirebaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseFirebaseEntity

class DiseaseBookRecyclerViewAdapter(private var dataList : List<DiseaseDetailFirebaseEntity>)
    : RecyclerView.Adapter<DiseaseBookRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(var binding : ItemcardDiseaseBookBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardDiseaseBookBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.apply {
            tvDiseasebookName.text = item.Name
            tvDiseasebookIlmiah.text = item.Bioname


        }
    }

    override fun getItemCount(): Int = dataList.size


}