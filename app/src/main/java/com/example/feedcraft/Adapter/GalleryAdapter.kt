package com.example.feedcraft.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.feedcraft.EditViewModel
import com.example.feedcraft.FilterModel
import com.example.feedcraft.GalleryModel
import com.example.feedcraft.R

class GalleryAdapter(context: Context?, private var galleryList: MutableList<GalleryModel>, private val clickListener: (position: Int, active: Boolean) -> Unit) :
    RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    companion object{
        var oneIsSelected: Boolean = false
        var itemSelected: GalleryModel? = null
    }

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val galleryImageView: ImageView = itemView.findViewById(R.id.imageItemGallery)
        var galleryImageColor: ImageView = itemView.findViewById(R.id.imageColorCode)
        val checkmark: ImageView = itemView.findViewById(R.id.imageViewCheckmark)

        fun bind(position: Int, galleryItem: GalleryModel, clickListener: (position: Int, active: Boolean) -> Unit) {
            galleryImageView.setOnClickListener {
                if(!galleryItem.isActive && !oneIsSelected && itemSelected == null){
                    checkmark.isVisible = true
                    oneIsSelected = true
                    itemSelected = galleryItem
                    galleryItem.isActive = true
                }
                else if(galleryItem.isActive && oneIsSelected && itemSelected == galleryItem){
                    checkmark.isVisible = false
                    oneIsSelected = false
                    itemSelected = null
                    galleryItem.isActive = false
                }
                clickListener(position, galleryItem.isActive)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        return GalleryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return galleryList.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val galleryItem: GalleryModel = galleryList[position]
        holder.bind(position, galleryItem, clickListener)
        holder.galleryImageView.setImageBitmap(galleryItem.imageBitmap)
        holder.galleryImageColor.setBackgroundColor(galleryItem.dominantColor)
    }

    fun galleryListChanged(galleryListChanged: MutableList<GalleryModel>){
        galleryList = galleryListChanged
    }

}
