package com.example.feedcraft

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.feedcraft.Constants.Companion.REQUEST_CAMERA
import com.example.feedcraft.Constants.Companion.REQUEST_GALLERY
import com.example.feedcraft.databinding.FragmentFeedDialogBinding


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
        //dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view = inflater.inflate(R.layout.fragment_feed_dialog, container, false)

        val parentLayout = view?.findViewById<View>(R.id.parent_feed_dialog)
        val feedDialog = view?.findViewById<View>(R.id.viewFeedDialog)

        //val imageView = view?.findViewById<ImageView>(R.id.image)
        val btnGallery = view?.findViewById<TextView>(R.id.textViewFeedGallery)
        val btnCamera = view?.findViewById<TextView>(R.id.textViewFeedCamera)



        parentLayout?.setOnClickListener {
            dismiss()
        }

        feedDialog?.setOnClickListener {
            // https://c1ctech.com/android-capture-image-from-camera-and-gallery/
            btnCamera?.setOnClickListener(View.OnClickListener {
                //To take picture from camera
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture, 0) //zero can be replaced with any action code
            })
            btnGallery?.setOnClickListener(View.OnClickListener {
                //To pick photo from gallery
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1) //one can be replaced with any action code
            })
        }

        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)

        return view
    }

    override fun onStart() {
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
                val extras: Bundle? = data?.extras
                val imageBitmap = extras?.get("data") as Bitmap?
                ((requireActivity().application) as UIApplication).tempBitmap = imageBitmap

                val intent = Intent(requireContext(), EditActivity::class.java).putExtra("CameraOrGallery", "Camera")
                startActivity(intent)

                findNavController().navigateUp()

            }
            else findNavController().navigateUp()

            REQUEST_GALLERY -> if (resultCode == RESULT_OK) {
                val selectedImageUri: Uri? = data?.data

                ((requireActivity().application) as UIApplication).imageUri = selectedImageUri
                val intent = Intent(requireContext(), EditActivity::class.java).putExtra("CameraOrGallery", "Gallery")
                startActivity(intent)

                findNavController().navigateUp()

            }
            else findNavController().navigateUp()

        }
    }

}