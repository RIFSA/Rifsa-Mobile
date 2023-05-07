package com.example.rifsa_mobile.view.fragment.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ItemcardFinanceBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity

class FinanceRecyclerViewAdapter(private var dataList : List<FinancialEntity>): RecyclerView.Adapter<FinanceRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(var binding : ItemcardFinanceBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var itemCallBack : ItemDetailCallback

    fun onItemCallBack(callback : ItemDetailCallback){
        this.itemCallBack = callback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardFinanceBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.tvcardFinanceTitle.text = item.name
        ("Rp "+ item.price).also { holder.binding.tvcardFinancePrice.text = it }

        if (item.type == "Pengeluaran"){
            holder.binding.imageView5.setImageResource(R.drawable.ic_finance_out)
        }else{
            holder.binding.imageView5.setImageResource(R.drawable.ic_finance_in)
        }

        holder.binding.tvcardFinanceTitle.setOnClickListener {
            itemCallBack.onItemCallback(item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    interface ItemDetailCallback{
        fun onItemCallback(data : FinancialEntity)
    }
}