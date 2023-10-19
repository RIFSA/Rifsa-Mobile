package com.example.rifsa_mobile.view.fragment.disease.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.databinding.ItemcardDisaseBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.DiseaseEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultPagedAdapter

class DiseasePagedAdapter:
    PagedListAdapter<DiseaseEntity,DiseasePagedAdapter.DiseaseViewHolder>(
        DiseaseComprator()
    ){

    private lateinit var itemCallBack
            : ItemDetailCallback

    fun onItemCallBack(
        callback : ItemDetailCallback
    ){
        this.itemCallBack = callback
    }


    class DiseaseViewHolder(val binding : ItemcardDisaseBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : DiseaseEntity){
            binding.tvDiseaseSubtitle.text = item.dateDisease
            binding.tvDiseaseTitle.text = item.nameDisease
        }
    }


    override fun onBindViewHolder(
        holder: DiseaseViewHolder,
        position: Int
    ) {
        val item = getItem(position) as DiseaseEntity
        holder.binding.tvDiseaseTitle.setOnClickListener {
            itemCallBack.onItemCallback(item)
        }
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiseaseViewHolder {
        return DiseaseViewHolder(
            ItemcardDisaseBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    class DiseaseComprator : DiffUtil.ItemCallback<DiseaseEntity>() {
        override fun areItemsTheSame(
            oldItem: DiseaseEntity,
            newItem: DiseaseEntity
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: DiseaseEntity,
            newItem: DiseaseEntity
        ): Boolean {
            return oldItem.localId === newItem.localId
        }

    }

    interface ItemDetailCallback{
        fun onItemCallback(data : DiseaseEntity)
    }
}