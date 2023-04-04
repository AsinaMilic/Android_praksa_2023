package com.example.feedcraft

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.feedcraft.repository.PreferenceDataStore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
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

    suspend fun getGalleryList(context: Context): ArrayList<GalleryModel>? = withContext(Dispatchers.IO) {
        val prefDataStore = PreferenceDataStore.getInstance(context)
        val galleryList: ArrayList<GalleryModel> = ArrayList()

        val json: String? = prefDataStore.getGsonImageData().firstOrNull()
        val objectArray = Gson().fromJson(json, Array<ImageData>::class.java)

        if(objectArray != null) {
            val objectList = objectArray.toList().sortedByDescending{it.IdCreation}

            for (imageDataObj in objectList) {
                val id: String = imageDataObj.IdCreation + ".png"
                val file = File(context.filesDir.toString()+ File.separator + "creation_previews", id)
                val fileOrg = File(context.filesDir.toString()+ File.separator + "saved_creations", id)

                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath) // decode the image file to a Bitmap object
                    val uri = Uri.fromFile(file)
                    val uriOrg = Uri.fromFile(fileOrg)
                    val dominantColor = 0x00000000//Color.RED
                    galleryList.add(GalleryModel(bitmap, dominantColor,uri, uriOrg))

                } else
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "File not found for ID: $id", Toast.LENGTH_SHORT).show()
                    }

            }
        }

        if (galleryList.isNotEmpty()) galleryList else null
    }

     fun extractFileName(uriString: String): String {
        val uri = Uri.parse(uriString)
        val path = uri.path
        val index = path?.lastIndexOf('/') ?: -1
        val fileName = if (index >= 0) path?.substring(index + 1) else path
        val dotIndex = fileName?.lastIndexOf('.') ?: -1
        return if (dotIndex > 0) fileName?.substring(0, dotIndex) ?: "" else fileName ?: ""
    }

}
