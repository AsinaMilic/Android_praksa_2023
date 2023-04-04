package com.example.feedcraft

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.feedcraft.dataModels.Edits

//this is at the highest level
class UIApplication : Application() {
    companion object {
        var imageUri: Uri? = null
        var tempBitmap: Bitmap? = null
        var galleryListChanged: Boolean = false
        var addPictureInNotification: Boolean = false

        val _saveCompleted = MutableLiveData<Boolean>()
        val saveCompleted: LiveData<Boolean> = _saveCompleted

        var edits: Edits? = null
    }

}