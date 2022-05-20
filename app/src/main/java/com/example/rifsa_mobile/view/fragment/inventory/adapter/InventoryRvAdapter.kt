package com.example.rifsa_mobile.view.fragment.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.rifsa_mobile.databinding.ItemcardInventoryBinding
import com.example.rifsa_mobile.model.entity.inventory.Inventory

class InventoryRvAdapter(private val dataList : List<Inventory>): RecyclerView.Adapter<InventoryRvAdapter.ViewHolder>() {
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
        holder.binding.tvcardInventTitle.text = item.name
        holder.binding.tvcardInventAmount.text = item.amount.toString()

        //todo 1.7 inventory bug
        Glide.with(holder.itemView.context)
            .asBitmap()
            .load(item.urlPhoto)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(holder.binding.imgcardInvent)



        holder.itemView.setOnClickListener {
            itemCallback.onDetailCallback(item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    interface OnDetailItemCallback{
        fun onDetailCallback(data : Inventory)
    }

}