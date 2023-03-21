package com.example.feedcraft

import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

data class GalleryModel(val imageBitmap: Bitmap, var dominantColor: Int, var isActive: Boolean = false) {}