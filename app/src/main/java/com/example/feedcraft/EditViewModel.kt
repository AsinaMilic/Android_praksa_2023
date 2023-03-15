package com.example.feedcraft

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class EditViewModel: ViewModel() {
    var bitmap: MutableLiveData<Bitmap> = MutableLiveData<Bitmap>(null)



    /*public fun getBitmap(): MutableLiveData<Bitmap> {
        return bitmap

    }*/
    fun getMapicu(): MutableLiveData<Bitmap> {
        return bitmap
    }
    fun setAnotherBitmapToLiveData(bitMapToReplaceWith: Bitmap){
        bitmap.value = bitMapToReplaceWith
    }


    val message: MutableLiveData<String> = MutableLiveData("")
    fun setAnotherValueToLiveData(messageToReplaceWith:String){
        message.value = messageToReplaceWith

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


}