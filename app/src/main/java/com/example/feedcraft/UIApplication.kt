package com.example.feedcraft

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri

//this is on the highest level
class UIApplication : Application() {
    companion object {
        var imageUri: Uri? = null
        var tempBitmap: Bitmap? = null
    }

}