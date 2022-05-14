package com.example.rifsa_mobile.view.fragment.finance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.databinding.ItemcardFinanceBinding
import com.example.rifsa_mobile.model.entity.finance.Finance

class FinanceRvAdapter(var dataList : List<Finance>): RecyclerView.Adapter<FinanceRvAdapter.ViewHolder>() {

    class ViewHolder(var binding : ItemcardFinanceBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardFinanceBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.tvcardFinancePrice.text = "Rp "+item.amount.toString()
        holder.binding.tvcardFinanceTitle.text = item.title
    }

    override fun getItemCount(): Int = dataList.size
}