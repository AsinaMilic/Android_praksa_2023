package com.example.feedcraft

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.net.URI

data class GalleryModel(val imageBitmap: Bitmap, var dominantColor: Int, val uri: Uri, var isActive: Boolean = false) {}