package com.example.feedcraft

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.feedcraft.Constants.Companion.REQUEST_CAMERA
import com.example.feedcraft.Constants.Companion.REQUEST_GALLERY
import com.example.feedcraft.databinding.FragmentFeedDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FeedDialogFragment : DialogFragment() {
    private var _binding: FragmentFeedDialogBinding? = null
    private val binding get() = _binding!!
    private var capturedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.full_screen_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_feed_dialog, container, false)
        val parentLayout = view?.findViewById<View>(R.id.parent_feed_dialog)
        val feedDialog = view?.findViewById<View>(R.id.viewFeedDialog)
        val btnGallery = view?.findViewById<TextView>(R.id.textViewDeleteOK)
        val btnCamera = view?.findViewById<TextView>(R.id.textViewDeleteCancel)

        parentLayout?.setOnClickListener {
            dismiss()
        }

        feedDialog?.setOnClickListener {
            //Empty body is needed
        }

        // https://c1ctech.com/android-capture-image-from-camera-and-gallery/
        btnCamera?.setOnClickListener {
            val fileName = "capturedImage.png"
            val file = File(context?.cacheDir.toString() + File.separator + fileName)
            if (file.exists())
                file.delete() // delete the existing file
            if (capturedImageUri != null)
                capturedImageUri = null
            capturedImageUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, file)
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri)
            startActivityForResult(takePicture, REQUEST_CAMERA)

        }
        btnGallery?.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 1)
        }

        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        return view
    }

    override fun onStart() { //forcing a layout to be fullscreen otherwise fragment wont be fullscreen
        super.onStart()
        dialog?.window?.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CAMERA -> if (resultCode == RESULT_OK) {
                /*CoroutineScope(Dispatchers.IO).launch {
                    UIApplication.tempBitmap = withContext(Dispatchers.IO) {
                        Glide
                            .with(requireContext())
                            .asBitmap()
                            .load(UIApplication.imageUri)
                            .submit()
                            .get()
                    }

                }
                findNavController().navigateUp()
                startActivity(Intent(requireContext(), EditActivity::class.java))*/
                UIApplication.imageUri = capturedImageUri
                startActivity(Intent(requireContext(), EditActivity::class.java))
            }

            REQUEST_GALLERY -> if (resultCode == RESULT_OK) {
                val selectedImageUri: Uri? = data?.data
                UIApplication.imageUri = selectedImageUri
                startActivity(Intent(requireContext(), EditActivity::class.java))
            }
        }
        findNavController().navigateUp()

    }
    private fun createTempImageFile(): String {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).absolutePath
    }

}