package com.example.rifsa_mobile.view.fragment.harvestresult.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.databinding.ItemcardFinanceBinding
import com.example.rifsa_mobile.databinding.ItemcardHasilBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestEntity
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinanceRecyclerViewAdapter
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinancialPagedAdapter

class HarvestResultPagedAdapter :
    PagedListAdapter<HarvestEntity,HarvestResultPagedAdapter.ViewHolder>(
        HarvestComprator()
    ) {

    private lateinit var itemCallBack
    : ItemDetailCallback

    fun onItemCallBack(
        callback : ItemDetailCallback
    ){
        this.itemCallBack = callback
    }

    class ViewHolder(val binding : ItemcardHasilBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : HarvestEntity){
            binding.tvCardhasilTitle.text = item.typeOfGrain
            binding.tvCardhasilDate.text = item.date
            ("${item.weight}" + " Kg").also { binding.tvCardhasilWeight.text = it }
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position) as HarvestEntity
        holder.binding.tvCardhasilTitle.setOnClickListener {
            itemCallBack.onItemCallback(item)
        }
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemcardHasilBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    class HarvestComprator : DiffUtil.ItemCallback<HarvestEntity>() {
        override fun areItemsTheSame(
            oldItem: HarvestEntity,
            newItem: HarvestEntity
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: HarvestEntity,
            newItem: HarvestEntity
        ): Boolean {
            return oldItem.localId === newItem.localId
        }

    }

    interface ItemDetailCallback{
        fun onItemCallback(data : HarvestEntity)
    }
}