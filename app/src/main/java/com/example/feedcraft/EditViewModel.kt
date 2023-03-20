package com.example.feedcraft

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feedcraft.Adapter.FilterAdapter
import com.example.feedcraft.databinding.FragmentEditBinding
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

    fun setCaptionText(textToReplaceWith: String){
        captionText.value = textToReplaceWith
    }


    fun setAnotherValueToLiveData(messageToReplaceWith:String){
        _message.value = messageToReplaceWith

    }

    fun saveBitmap(bitmap: Bitmap, filePath: String, fileName: String): File {

        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

        //val previewBitmap: Bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true) //true or false idk

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
    //TODO merge these 2 functions
    fun saveBitmapPreview(bitmap: Bitmap, filePath: String, fileName: String, size: Int): File {

        val bytes = ByteArrayOutputStream()
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, size, size, false)
        scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

        val dir = File(filePath)

        if (!dir.exists()) {
            dir.mkdir()
        }
        val newFileName:String = System.currentTimeMillis().toString()+"_"+fileName
        val f = File(dir, newFileName)
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

     fun addDataToList(activity: FragmentActivity, context: Context?, filterList: ArrayList<FilterModel> ){
        val cameraOrGallery: String = activity.intent?.extras?.getString("CameraOrGallery").toString()
        val previewBitMap: Bitmap

        if(cameraOrGallery == "Gallery") {
            val selectedImageFromGalleryUri = UIApplication.imageUri
            previewBitMap = MediaStore.Images.Media.getBitmap(context?.contentResolver,selectedImageFromGalleryUri) //valjda ce radi
            //Glide.with(requireActivity()).load(selectedImageFromGalleryUri).into(binding.imageViewToEdit)
        }
        else {
            previewBitMap = UIApplication.tempBitmap!!
        }

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