package com.example.rifsa_mobile.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.databinding.ItemcardHasilBinding
import com.example.rifsa_mobile.model.local.entity.harvestresult.HarvestResultMock

class HarvestResultRvAdapter(var dataList : List<HarvestResultMock>): RecyclerView.Adapter<HarvestResultRvAdapter.ViewHolder>() {
    class ViewHolder(var bindig : ItemcardHasilBinding): RecyclerView.ViewHolder(bindig.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardHasilBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bindig.tvCardhasilTitle.text = item.title
        holder.bindig.tvCardhasilDate.text = item.date
        holder.bindig.tvCardhasilWeight.text = item.weight.toString()

    }

    override fun getItemCount(): Int = dataList.size
}