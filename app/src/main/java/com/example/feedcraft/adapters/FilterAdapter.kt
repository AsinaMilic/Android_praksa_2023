package com.example.feedcraft.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.feedcraft.dataModels.FilterModel
import com.example.feedcraft.R

class FilterAdapter(private val filterList: MutableList<FilterModel>, private val clickListener: (position: Int) -> Unit) :
    RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    class FilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val filterImageView: ImageView = itemView.findViewById(R.id.filterImageView)
        val filterTextView: TextView = itemView.findViewById(R.id.textViewFilter)

        fun bind( position: Int, clickListener: (position: Int) -> Unit) = filterImageView.setOnClickListener { clickListener(position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_filter_item, parent, false)
        return FilterViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filter: FilterModel = filterList[position]
        holder.bind(position, clickListener)
        holder.filterImageView.setImageBitmap(filter.imageBitmap)
        holder.filterTextView.text = filter.filterName

    }
}