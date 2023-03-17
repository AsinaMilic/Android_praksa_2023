package com.example.feedcraft

import android.graphics.Bitmap

data class Edits(var uri:String,
            var bitmap: Bitmap,
            var caption: String,
            var filter: String,
            var brightness: Int,
            var saturation: Int,
            var contrast: Int)
{

}