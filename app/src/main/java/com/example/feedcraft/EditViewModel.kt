package com.example.feedcraft

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class EditViewModel: ViewModel() {
    val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String>
        get() = _message

    val captionText: MutableLiveData<String> = MutableLiveData()


    var addPictureNotification: Boolean = false

    fun setCaptionText(textToReplaceWith: String){
        captionText.value = textToReplaceWith
    }


    fun setAnotherValueToLiveData(messageToReplaceWith:String){
        _message.value = messageToReplaceWith

    }

    fun saveBitmap(bitmap: Bitmap, filePath: String, newFileName:String): File {

        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

        //val previewBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true) //true or false idk

        val dir = File(filePath)

        if (!dir.exists()) {
            dir.mkdir()
        }

        val f = File(dir, "$newFileName.png")
        f.createNewFile()

        val fo = FileOutputStream(f)
        fo.write(bytes.toByteArray())
        fo.close()

        return f
    }
    //TODO merge these 2 functions
    fun saveBitmapPreview(bitmap: Bitmap, filePath: String, size: Int, newFileName:String): File {

        val bytes = ByteArrayOutputStream()
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, size, size, false)
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

        val dir = File(filePath)

        if (!dir.exists()) {
            dir.mkdir()
        }
        //val newFileName:String = System.currentTimeMillis().toString()
        val f = File(dir, "$newFileName.png")
        f.createNewFile()

        val fo = FileOutputStream(f)
        fo.write(bytes.toByteArray())
        fo.close()

        return f
    }

    fun save2SharedPreferences(context: Context, caption: String, idCreation: String) {
        // Get the existing list of objects from shared preferences
        val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val json = prefs.getString("my_list_key", null)

        // Create a new object to add to the list
        val myObject = ImageData(idCreation, caption)

        if (json != null) {
            // If the JSON string is not null, parse it into a list of objects
            val objectArray = Gson().fromJson(json, Array<ImageData>::class.java).toMutableList()

            objectArray.add(myObject)
            // Convert the list back to a JSON string and save it to shared preferences
            val updatedJson = Gson().toJson(objectArray)
            prefs.edit().putString("my_list_key", updatedJson).apply()
        } else {
            val objectList = mutableListOf(myObject)
            val updatedJson = Gson().toJson(objectList)
            prefs.edit().putString("my_list_key", updatedJson).apply()
        }

    }

    fun shareImage(intent: Intent, context: Context?){
        val uri:Uri? = UIApplication.imageUri //Uri from gallery

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context?.startActivity(sendIntent) //nece sharing iz edit
        }catch(activityNotFoundEx: ActivityNotFoundException){
            val text = "something bad happened! :("
            val duration = Toast.LENGTH_SHORT
            Toast.makeText(context, text, duration).show()
        }

    }

     fun addDataToList(activity: FragmentActivity, context: Context?, filterList: ArrayList<FilterModel> ){
        val previewBitMap: Bitmap
        val selectedImageFromGalleryUri = UIApplication.imageUri
        previewBitMap = MediaStore.Images.Media.getBitmap(context?.contentResolver,selectedImageFromGalleryUri) //valjda ce radi

        val filters = listOf(
            GPUImageGrayscaleFilter(),
            GPUImageColorBalanceFilter(),
            GPUImageSepiaToneFilter(),
            GPUImageSwirlFilter(),
            GPUImageContrastFilter(),
            GPUImageEmbossFilter(),
            GPUImagePosterizeFilter(),
            GPUImageVibranceFilter(),
            GPUImageLaplacianFilter(),
            GPUImageColorInvertFilter()
        )
        val filteredBitmaps = applyFilterToBitmap(context, previewBitMap, filters)


        filterList.add(FilterModel(previewBitMap,"Normal"))
        filterList.add(FilterModel(filteredBitmaps[0],"GrayScale"))
        filterList.add(FilterModel(filteredBitmaps[1],"Balance"))
        filterList.add(FilterModel(filteredBitmaps[2],"Sepia"))

        filterList.add(FilterModel(filteredBitmaps[3],"Swirl"))
        filterList.add(FilterModel(filteredBitmaps[4],"Contrast"))
        filterList.add(FilterModel(filteredBitmaps[5],"Emboss"))
        filterList.add(FilterModel(filteredBitmaps[6],"Posterize"))

        filterList.add(FilterModel(filteredBitmaps[7],"Vibrance"))
        filterList.add(FilterModel(filteredBitmaps[8],"Laplacian"))
        filterList.add(FilterModel(filteredBitmaps[9],"Invert"))
    }

    private fun applyFilterToBitmap(context: Context?,bitmap: Bitmap, filters: List<GPUImageFilter>): ArrayList<Bitmap> {
        val filteredImageList = ArrayList<Bitmap>()
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)
        for (filter in filters) {
            gpuImage.setFilter(filter)
            filteredImageList.add(gpuImage.bitmapWithFilterApplied)
        }
        return filteredImageList
    }




}