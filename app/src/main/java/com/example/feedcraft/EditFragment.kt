package com.example.feedcraft

import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.getIntentOld
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.databinding.FragmentEditBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide


class EditFragment :  Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var seekBar: SeekBar
    private lateinit var percentageTextView: TextView

    private val viewModel: EditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        seekBar = binding.seekBar
        percentageTextView = binding.textViewPercentage
        seekBar.max = 100
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                percentageTextView.text = "$progress%"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ViewCaption.setOnClickListener{
            val action = EditFragmentDirections.actionEditFragmentToAddCaptionDialogFragment()
            findNavController().navigate(action)
        }

        binding.imageViewFinish.setOnClickListener{

            //val cameraImage = ((requireActivity().application) as UIApplication).tempBitmap
            val cameraImage = UIApplication.editedImage?.bitmap
           /* UIApplication.editedImage.bitmap = "zlj"*/
            viewModel.setAnotherBitmapToLiveData(cameraImage!!)



            val action = EditFragmentDirections.actionEditFragmentToFinishFragment()
            findNavController().navigate(action)
        }

        binding.imageViewBack.setOnClickListener {
            activity?.finish()
        }

        //ovde upada
        /*viewModel.bitmap.observe(viewLifecycleOwner){ newBitmap->
            Toast.makeText(requireContext(), "Stigla je bitmapa u newBitmap?", Toast.LENGTH_LONG).show()
            val a = 1
            val b = 3
        }*/

        val cameraOrGallery: String = requireActivity().intent?.extras?.getString("CameraOrGallery").toString()

        if(cameraOrGallery == "Gallery") {
            val selectedImageFromGalleryUri = UIApplication.imageUri
            Glide.with(requireActivity()).load(selectedImageFromGalleryUri).into(binding.imageViewToEdit)
        }
        else {
            val cameraImage = UIApplication.editedImage?.bitmap
            //Glide.with(requireActivity()).load(cameraImage).into(binding.imageViewToEdit)
            binding.imageViewToEdit.setImageBitmap(cameraImage)
        }

    }

}