package com.example.rifsa_mobile.view.fragment.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.databinding.ItemcardInventoryBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryEntity

class InventoryRecyclerViewAdapter(private val dataList : List<InventoryEntity>)
    : RecyclerView.Adapter<InventoryRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(val binding : ItemcardInventoryBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var itemCallback : OnDetailItemCallback

    fun onItemDetailCallback(callback : OnDetailItemCallback){
        this.itemCallback = callback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardInventoryBinding.inflate(
            LayoutInflater.from(parent.context
            )
            ,parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = dataList[position]
        holder.binding.tvcardInventTitle.text = item.name
        holder.binding.tvcardInventAmount.text = item.amount

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.binding.imgcardInvent)

        holder.itemView.setOnClickListener {
            itemCallback.onDetailCallback(item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    interface OnDetailItemCallback{
        fun onDetailCallback(data : InventoryEntity)
    }

}