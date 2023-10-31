package com.example.rifsa_mobile.view.fragment.inventory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rifsa_mobile.databinding.ItemcardInventoryBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.InventoryEntity

class InventoryPagedAdapter:
    PagingDataAdapter<InventoryEntity,InventoryPagedAdapter.ViewHolder>(
        InventoryComprator()
    )
{

    private lateinit var itemCallBack: ItemDetailCallback

    fun onItemCallBack(
        callback : ItemDetailCallback
    ){
        this.itemCallBack = callback
    }

    class ViewHolder(
        val binding : ItemcardInventoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item : InventoryEntity){
            binding.tvcardInventTitle.text = item.name
            binding.tvcardInventAmount.text = item.amount

            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.imgcardInvent)
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position) as InventoryEntity
        holder.binding.tvcardInventTitle.setOnClickListener {
            itemCallBack.onItemCallback(item)
        }
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       return ViewHolder(ItemcardInventoryBinding.inflate(
           LayoutInflater.from(parent.context)
       )
       )
    }

    class InventoryComprator : DiffUtil.ItemCallback<InventoryEntity>() {
        override fun areItemsTheSame(
            oldItem: InventoryEntity,
            newItem: InventoryEntity
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: InventoryEntity,
            newItem: InventoryEntity
        ): Boolean {
            return oldItem === newItem
        }
    }

    interface ItemDetailCallback{
        fun onItemCallback(data : InventoryEntity)
    }

}