package com.example.feedcraft

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feedcraft.repository.PreferenceDataStore
import com.google.gson.Gson
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
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

    fun saveEditedPicture(context: Context) {
        val fileName: String = System.currentTimeMillis().toString()
        UIApplication.tempBitmap?.let {
            GlobalScope.launch(Dispatchers.IO) {
                saveBitmapOrPreview(context, it, fileName)
                saveBitmapOrPreview(context, it, fileName, 300)
                save2prefDataStore(context, "hardcoded_caption", fileName)
                UIApplication._saveCompleted.postValue(true)

            }
        }
        UIApplication.imageUri = null
        UIApplication.tempBitmap = null
        UIApplication.galleryListChanged = true
    }

    private fun saveBitmapOrPreview(context: Context, bitmap: Bitmap, fileName: String, size: Int? = null): File {
        var filePath: String = context.filesDir.toString() + File.separator
        val bytes = ByteArrayOutputStream()

        val bitmapToSave: Bitmap
        if (size != null) {
            bitmapToSave = Bitmap.createScaledBitmap(bitmap, size, size, false)
            filePath += "creation_previews"
        } else {
            bitmapToSave = bitmap
            filePath += "saved_creations"
        }

        bitmapToSave.compress(Bitmap.CompressFormat.PNG, 90, bytes)

        val dir = File(filePath)
        if (!dir.exists()) {
            dir.mkdir()
        }

        val file = File(dir, "$fileName.png")
        file.createNewFile()

        FileOutputStream(file).use { it.write(bytes.toByteArray()) }

        return file
    }

    private suspend fun save2prefDataStore(context: Context, caption: String, idCreation: String) {
        val prefDataStore = PreferenceDataStore.getInstance(context)
        val json: String? = prefDataStore.getGsonImageData().firstOrNull()

        val myObject = ImageData(idCreation, caption)

        val objectList = if (json != null) { //if array exist, add ImageData
            Gson().fromJson(json, Array<ImageData>::class.java).toMutableList().apply { add(myObject) }
        } else { //else make new array
            mutableListOf(myObject)
        }

        val updatedJson = Gson().toJson(objectList)
        prefDataStore.setGsonImageData(updatedJson)
    }

    fun shareImage(intent: Intent, context: Context?){
        val uri:Uri? = UIApplication.imageUri //Uri from gallery

        val contentUri = FileProvider.getUriForFile(
            context!!,
            context.packageName,
            File(uri!!.path!!)
        )

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "image/png"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(Intent.createChooser(sendIntent, "Share image"))
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