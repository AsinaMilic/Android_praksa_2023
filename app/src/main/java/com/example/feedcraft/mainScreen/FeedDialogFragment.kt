package com.example.feedcraft.mainScreen

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.BuildConfig
import com.example.feedcraft.R
import com.example.feedcraft.UIApplication
import com.example.feedcraft.editScreen.EditActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

const val REQUEST_CAMERA = 0
const val REQUEST_GALLERY = 1

class FeedDialogFragment : DialogFragment() {
    private var capturedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.full_screen_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //without binding
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
            lifecycleScope.launch(Dispatchers.IO) {
                val fileName = "capturedImage.png"
                val file = File(requireContext().cacheDir, fileName)
                capturedImageUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, file)
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri)
                }
                startActivityForResult(takePicture, REQUEST_CAMERA)
            }
        }

        btnGallery?.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, REQUEST_GALLERY)
                }
            }
        }

        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        return view
    }

    override fun onStart() { //forcing a layout to be fullscreen otherwise fragment wont be fullscreen
        super.onStart()
        dialog?.window?.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CAMERA -> if (resultCode == RESULT_OK) {
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

}