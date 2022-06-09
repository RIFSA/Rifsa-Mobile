package com.example.rifsa_mobile.view.fragment.finance.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ItemcardFinanceBinding
import com.example.rifsa_mobile.model.entity.remote.finance.FinanceResponseData

class FinanceRecyclerViewAdapter(private var dataList : List<FinanceResponseData>): RecyclerView.Adapter<FinanceRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(var binding : ItemcardFinanceBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var itemCallBack : ItemDetailCallback

    fun onItemCallBack(callback : ItemDetailCallback){
        this.itemCallBack = callback
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardFinanceBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.tvcardFinanceTitle.text = item.kegiatan
        holder.binding.tvcardFinancePrice.text = "Rp "+ item.jumlah

        if (item.jenis == "Pengeluaran"){
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
        fun onItemCallback(data : FinanceResponseData)
    }
}