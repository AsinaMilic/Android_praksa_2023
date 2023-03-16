package com.example.feedcraft

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class EditViewModel: ViewModel() {
    val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String>
        get() = _message

    fun setAnotherValueToLiveData(messageToReplaceWith:String){
        _message.value = messageToReplaceWith

    }

    fun saveBitmap(bitmap: Bitmap, filePath: String, fileName: String): File {

        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

        val dir = File(filePath)

        if (!dir.exists()) {
            dir.mkdir()
        }

        val f = File(dir, fileName)
        f.createNewFile()

        val fo = FileOutputStream(f)
        fo.write(bytes.toByteArray())
        fo.close()

        return f
    }

    fun shareImage(intent: Intent, context: Context?){
        var uri:Uri? = UIApplication.imageUri //Uri from gallery

        val cameraOrGallery: String = intent.extras?.getString("CameraOrGallery").toString()
        if(cameraOrGallery == "Camera") {
            val filePath = context?.filesDir.toString() + File.separator + "saved_creations" + File.separator + "creation_1.png"
            uri = context?.let { FileProvider.getUriForFile(it, BuildConfig.APPLICATION_ID, File(filePath)) }
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
        }

        try {
            context?.startActivity(sendIntent)
        }catch(activityNotFoundEx: ActivityNotFoundException){
            val text = "something bad happened! :("
            val duration = Toast.LENGTH_SHORT
            Toast.makeText(context, text, duration).show()
        }

    }


}