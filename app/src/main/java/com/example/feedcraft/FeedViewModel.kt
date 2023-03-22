package com.example.feedcraft

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import java.io.File

class FeedViewModel : ViewModel() {
    val deletePicture = MutableLiveData<Boolean>()
    var dominantColor: MutableLiveData<Int> = MutableLiveData()

    fun DeleteOrCancel(deleteCancelOk: Boolean){
        deletePicture.value = deleteCancelOk
    }

    fun getDominantColor(bitmap: Bitmap): Int{
        val _dominantColor = bitmap.getPixel(0, 0)
        dominantColor.value = _dominantColor
        return _dominantColor
    }

    fun getGalleryList(context: Context): ArrayList<GalleryModel>? {
        val galleryList: ArrayList<GalleryModel> = ArrayList()
        val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val json = prefs?.getString("my_list_key", null)
        val objectArray = Gson().fromJson(json, Array<ImageData>::class.java)
        if(objectArray != null) {
            val objectList = objectArray.toList().sortedByDescending{it.IdCreation}

            for (imageDataObj in objectList) {
                val id: String = imageDataObj.IdCreation + ".png"
                val file = File(context.filesDir.toString()+ File.separator + "creation_previews", id)

                if (file.exists()) {

                    val bitmap = BitmapFactory.decodeFile(file.absolutePath) // decode the image file to a Bitmap object
                    val uri = Uri.fromFile(file)
                    val dominantColor = 0x00000000
                    galleryList.add(GalleryModel(bitmap, dominantColor,uri))

                } else {
                    Toast.makeText(context, "File not found for ID: $id", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else
            return null
        return galleryList
    }


}
