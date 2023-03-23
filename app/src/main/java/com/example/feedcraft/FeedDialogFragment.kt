package com.example.feedcraft

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.feedcraft.Constants.Companion.REQUEST_CAMERA
import com.example.feedcraft.Constants.Companion.REQUEST_GALLERY
import com.example.feedcraft.UIApplication.Companion.imageUri
import com.example.feedcraft.databinding.FragmentFeedDialogBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class FeedDialogFragment : DialogFragment() {
    private var _binding: FragmentFeedDialogBinding? = null
    private val binding get() = _binding!!

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
            // https://c1ctech.com/android-capture-image-from-camera-and-gallery/
            btnCamera?.setOnClickListener {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0) //zero can be replaced with any action code
            }
            btnGallery?.setOnClickListener(View.OnClickListener {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)
            })
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
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "New Picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
                val imageUri = context?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                // Spremanje privremene putanje slike u aplikacijskoj klasi
                val tempFilePath = createTempImageFile()
                UIApplication.tempFilePath = tempFilePath

                // Pokretanje kamere s privremenom putanjom
                val photoUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, File(tempFilePath))
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                startActivity(cameraIntent)
            }
            /*val extras: Bundle? = data?.extras
                val imageBitmap = extras?.get("data") as Bitmap?
                UIApplication.tempBitmap = imageBitmap!!
                startActivity(Intent(requireContext(), EditActivity::class.java).putExtra("CameraOrGallery", "Camera"))*/


            REQUEST_GALLERY -> if (resultCode == RESULT_OK) {
                val selectedImageUri: Uri? = data?.data
                UIApplication.imageUri = selectedImageUri
                startActivity(Intent(requireContext(), EditActivity::class.java).putExtra("CameraOrGallery", "Gallery"))
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