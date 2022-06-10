package com.example.rifsa_mobile.view.fragment.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.databinding.ItemcardInventoryBinding
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryResultResponData

class inventoryRecyclerViewAdapter(private val dataList : List<InventoryResultResponData>): RecyclerView.Adapter<inventoryRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(val binding : ItemcardInventoryBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var itemCallback : OnDetailItemCallback

    fun onItemDetailCallback(callback : OnDetailItemCallback){
        this.itemCallback = callback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardInventoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = dataList[position]
        holder.binding.tvcardInventTitle.text = item.nama
        holder.binding.tvcardInventAmount.text = item.jumlah

        val url = "http://34.101.50.17:5000/images/${item.image}"
        Glide.with(holder.itemView.context)
            .load(url)
            .dontAnimate()
            .into(holder.binding.imgcardInvent)

        holder.itemView.setOnClickListener {
            itemCallback.onDetailCallback(item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    interface OnDetailItemCallback{
        fun onDetailCallback(data : InventoryResultResponData)
    }

}