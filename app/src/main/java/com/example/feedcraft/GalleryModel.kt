package com.example.feedcraft

import android.graphics.Bitmap

data class GalleryModel(val imageBitmap: Bitmap, val dominantColor: Int, var isActive: Boolean = false) {
}