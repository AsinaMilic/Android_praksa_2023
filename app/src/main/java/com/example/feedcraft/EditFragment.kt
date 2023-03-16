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
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.databinding.FragmentEditBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide

class EditFragment :  Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var seekBar: SeekBar
    private lateinit var percentageTextView: TextView
    private val viewModel: EditViewModel by activityViewModels() //no ViewModels!
    private var brightnessSelected: Boolean = false
    private var saturationSelected: Boolean = false
    private var contrastSelected: Boolean = false

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

            val cameraImage = UIApplication.tempBitmap

            val action = EditFragmentDirections.actionEditFragmentToFinishFragment()
            findNavController().navigate(action)
        }

        binding.imageViewBack.setOnClickListener {
            activity?.finish()
        }

        val cameraOrGallery: String = requireActivity().intent?.extras?.getString("CameraOrGallery").toString()

        if(cameraOrGallery == "Gallery") {
            val selectedImageFromGalleryUri = UIApplication.imageUri
            Glide.with(requireActivity()).load(selectedImageFromGalleryUri).into(binding.imageViewToEdit)
        }
        else {
            val cameraImage = UIApplication.tempBitmap
            binding.imageViewToEdit.setImageBitmap(cameraImage) //Glide.with(requireActivity()).load(cameraImage).into(binding.imageViewToEdit)
        }

        val filterView = binding.ViewFilter
        filterView.setOnClickListener {
            //binding.editOptions.isVisible = false
            //binding.filterOptions.isVisible = true
            binding.seekBar.isVisible = false
            percentageTextView.isVisible = false
        }

        val brightnessView = binding.ViewBrightness
        brightnessView.setOnClickListener {
            openEditOptions()
            setBrightnessSelected()
            val brightnessValue = 25//editorViewModel.getCreationBrightness()
            seekBar.progress = brightnessValue
            percentageTextView.text = "$brightnessValue%"
        }

        val saturationView = binding.ViewSaturation
        saturationView.setOnClickListener {
            openEditOptions()
            setSaturationSelected()
            val saturationValue = 26//editorViewModel.getCreationSaturation()
            seekBar.progress = saturationValue
            percentageTextView.text = "$saturationValue%"
        }

        val constrastView = binding.ViewContrast
        constrastView.setOnClickListener {
            openEditOptions()
            setContrastSelected()
            val contrastValue = 27//editorViewModel.getCreationContrast()
            seekBar.progress = contrastValue
            percentageTextView.text = "$contrastValue%"
        }


    }

    private fun openEditOptions() {
        //binding.editOptions.isVisible = true
        binding.seekBar.isVisible = true
        percentageTextView.isVisible = true
        //binding.filterOptions.isVisible = false
    }

    private fun setBrightnessSelected(){
        brightnessSelected = true
        saturationSelected = false
        contrastSelected = false
    }

    private fun setSaturationSelected(){
        brightnessSelected = false
        saturationSelected = true
        contrastSelected = false
    }

    private fun setContrastSelected(){
        brightnessSelected = false
        saturationSelected = false
        contrastSelected = true
    }

}