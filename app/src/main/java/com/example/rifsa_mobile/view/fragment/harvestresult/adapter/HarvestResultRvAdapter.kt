package com.example.rifsa_mobile.view.fragment.harvestresult.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.databinding.ItemcardHasilBinding
import com.example.rifsa_mobile.model.entity.harvestresult.HarvestResult

class HarvestResultRvAdapter(private var dataList : List<HarvestResult>): RecyclerView.Adapter<HarvestResultRvAdapter.ViewHolder>() {
    class ViewHolder(var binding : ItemcardHasilBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var itemCallBak : OnDetailCallback

    fun onDetailCallBack(callback : OnDetailCallback){
        this.itemCallBak = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardHasilBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.tvCardhasilTitle.text = item.title
        holder.binding.tvCardhasilDate.text = item.date
        holder.binding.tvCardhasilWeight.text = item.weight.toString() + " Kg"

        holder.binding.tvCardhasilTitle.setOnClickListener {
            itemCallBak.onDetailCallback(item)
        }
    }

    override fun getItemCount(): Int = dataList.size


    interface OnDetailCallback{
        fun onDetailCallback(data : HarvestResult)
    }
}