package com.example.rifsa_mobile.view.fragment.disase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.ItemcardDisaseBinding
import com.example.rifsa_mobile.model.entity.disase.Disease

class DiseaseRvAdapter(private var dataList : List<Disease>): RecyclerView.Adapter<DiseaseRvAdapter.ViewHolder>() {
    class ViewHolder(var binding : ItemcardDisaseBinding): RecyclerView.ViewHolder(binding.root)

    private lateinit var diseaseDetail : OnDetailCallback

    fun onDiseaseDetailCallback(callback : OnDetailCallback){
        this.diseaseDetail = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemcardDisaseBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.apply {
            tvDiseaseSubtitle.text = item.date
            tvDiseaseTitle.text = item.indication

            if (item.reminder){
                holder.binding.imgIcondisease.setImageResource(R.drawable.ic_warning)
            }
        }

        holder.itemView.setOnClickListener {
            diseaseDetail.onDetailCallback(item)
        }
    }

    override fun getItemCount(): Int = dataList.size


    interface OnDetailCallback{
        fun onDetailCallback(data : Disease)
    }

}