package com.example.rifsa_mobile.view.fragment.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.databinding.ItemcardInventoryBinding
import com.example.rifsa_mobile.model.entity.inventory.Inventory

class InventoryRvAdapter(val dataList : List<Inventory>): RecyclerView.Adapter<InventoryRvAdapter.ViewHolder>() {
    class ViewHolder(val binding : ItemcardInventoryBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardInventoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = dataList[position]
        holder.binding.tvcardInventTitle.text = item.name
        holder.binding.tvcardInventAmount.text = item.amount.toString()

        Glide.with(holder.itemView.context)
            .asBitmap()
            .load(item.urlPhoto)
            .into(holder.binding.imgcardInvent)
    }

    override fun getItemCount(): Int = dataList.size

}