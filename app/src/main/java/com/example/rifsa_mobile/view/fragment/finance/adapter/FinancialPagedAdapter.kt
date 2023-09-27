package com.example.rifsa_mobile.view.fragment.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ItemcardFinanceBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity

class FinancialPagedAdapter:
    PagedListAdapter<FinancialEntity, FinancialPagedAdapter.ViewHolder>(FinancialComprator()) {

    private lateinit var itemCallBack : FinanceRecyclerViewAdapter.ItemDetailCallback

    fun onItemCallBack(callback : FinanceRecyclerViewAdapter.ItemDetailCallback){
        this.itemCallBack = callback
    }

    class ViewHolder(var binding : ItemcardFinanceBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(item: FinancialEntity){
                binding.tvcardFinanceTitle.text = item.name
                binding.tvcardFinanceDate.text = "${item.date}"
                ("Rp "+ item.price).also { binding.tvcardFinancePrice.text = it }

                if (item.type == "Pengeluaran"){
                    binding.imageView5.setImageResource(R.drawable.ic_finance_out)
                }else{
                    binding.imageView5.setImageResource(R.drawable.ic_finance_in)
                }
            }
   }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) as FinancialEntity
        holder.binding.tvcardFinanceTitle.setOnClickListener {
           itemCallBack.onItemCallback(item)
        }
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemcardFinanceBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    class FinancialComprator : DiffUtil.ItemCallback<FinancialEntity>() {
        override fun areItemsTheSame(oldItem: FinancialEntity, newItem: FinancialEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: FinancialEntity,
            newItem: FinancialEntity
        ): Boolean {
            return oldItem.localId === newItem.localId
        }

    }


    interface ItemDetailCallback{
        fun onItemCallback(data : FinancialEntity)
    }
}