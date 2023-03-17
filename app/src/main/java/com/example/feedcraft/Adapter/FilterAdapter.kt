package com.example.feedcraft.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.feedcraft.FilterModel
import com.example.feedcraft.R

class FilterAdapter(private val filterList: MutableList<FilterModel>) :
    RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {
    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val filterImageView: ImageView = itemView.findViewById(R.id.filterImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_filter_item, parent, false)
        return FilterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filter = filterList[position]
        holder.filterImageView.setImageBitmap(filter.imageBitmap) //setImageBitmap
    }
}