package com.example.feedcraft

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.databinding.FragmentEditBinding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.feedcraft.Adapter.FilterAdapter
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*

class EditFragment :  Fragment() {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var seekBar: SeekBar
    private lateinit var percentageTextView: TextView
    private val viewModel: EditViewModel by activityViewModels() //no ViewModels!
    private var brightnessSelected: Boolean = false
    private var saturationSelected: Boolean = false
    private var contrastSelected: Boolean = false

    var brightnessPercentage = 0
    var saturationPercentage = 0
    var contrastPercentage = 0
    private lateinit var gpuImage: GPUImage

    private lateinit var recyclerView: RecyclerView
    private lateinit var filterList: ArrayList<FilterModel>
    private lateinit var filterAdapter: FilterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        init()

        seekBar = binding.seekBar
        percentageTextView = binding.textViewPercentage
        seekBar.progress = 0
        seekBar.max = 100

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

        viewModel.captionText.observe(viewLifecycleOwner) {
                caption -> binding.textViewCaption.text = caption

            setGpuImage()

            val textBitmap = Bitmap.createBitmap(binding.imageViewToEdit.width, binding.imageViewToEdit.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(textBitmap)
            val textPaint = Paint().apply {
                color = Color.BLUE
                textSize = 50f
                style = Paint.Style.FILL
                isAntiAlias = true
            }
            canvas.drawText(binding.textViewCaption.text.toString(), 50f, 50f, textPaint)

            // Merge the text bitmap with the original image using a GPUImageTwoInputFilter
            val blendFilter = GPUImageAddBlendFilter()
            blendFilter.bitmap = textBitmap
            gpuImage.setFilter(blendFilter)

            binding.imageViewToEdit.setImageBitmap(gpuImage.bitmapWithFilterApplied)
            TODO("sacuvati promene nad slikom")
        }

        binding.ViewCaption.setOnClickListener{
            val action = EditFragmentDirections.actionEditFragmentToAddCaptionDialogFragment()
            findNavController().navigate(action)
        }

        binding.ViewFilter.setOnClickListener {
            recyclerView.isVisible = true
            binding.seekBar.isVisible = false
            percentageTextView.isVisible = false
        }

        binding.ViewBrightness.setOnClickListener {
            openEditOptions()
            setBrightnessSelected()
            seekBar.setOnSeekBarChangeListener(null)
            seekBar.progress = brightnessPercentage
            percentageTextView.text = "$brightnessPercentage%"

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    percentageTextView.text = "$progress%"
                    brightnessPercentage = progress
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })

            gpuImage = setGpuImage()

            val brightnessFilter = GPUImageBrightnessFilter()
            val brightnessLevel = ((brightnessPercentage / 100.0f) * 2) - 1
            brightnessFilter.setBrightness(brightnessLevel)
            gpuImage.setFilter(brightnessFilter)

            binding.imageViewToEdit.setImageBitmap(gpuImage.bitmapWithFilterApplied)
        }

        binding.ViewSaturation.setOnClickListener {
            openEditOptions()
            setSaturationSelected()
            seekBar.setOnSeekBarChangeListener(null) //its important for this line to stay here and not two lines down below
            seekBar.progress = saturationPercentage
            percentageTextView.text = "$saturationPercentage%"
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    percentageTextView.text = "$progress%"
                    saturationPercentage = progress
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })

            gpuImage = setGpuImage()

            val saturationFilter = GPUImageSaturationFilter()
            saturationFilter.setSaturation(saturationPercentage/50.0f)
            gpuImage.setFilter(saturationFilter)

            binding.imageViewToEdit.setImageBitmap(gpuImage.bitmapWithFilterApplied)

        }

        binding.ViewContrast.setOnClickListener {
            openEditOptions()
            setContrastSelected()
            seekBar.setOnSeekBarChangeListener(null)
            seekBar.progress = contrastPercentage
            percentageTextView.text = "$contrastPercentage%"
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    percentageTextView.text = "$progress%"
                    contrastPercentage = progress
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }

    }

    private fun openEditOptions() {
        recyclerView.isVisible = false
        binding.seekBar.isVisible = true
        percentageTextView.isVisible = true
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
    private fun init(){
        recyclerView = binding.filterRecycleView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        filterList = ArrayList()

        viewModel.addDataToList(requireActivity(), context, filterList)

        filterAdapter = FilterAdapter(filterList){ position ->
            val clickedImage = filterList[position].imageBitmap
            binding.imageViewToEdit.setImageBitmap(clickedImage)
        }
        recyclerView.adapter = filterAdapter
    }

    private fun setGpuImage(): GPUImage {
        val gpuImage = GPUImage(context)
        val cameraOrGallery: String = requireActivity().intent?.extras?.getString("CameraOrGallery").toString()
        if(cameraOrGallery == "Gallery")
            gpuImage.setImage(UIApplication.imageUri)
        else
            gpuImage.setImage(UIApplication.tempBitmap)
        return gpuImage
    }

}