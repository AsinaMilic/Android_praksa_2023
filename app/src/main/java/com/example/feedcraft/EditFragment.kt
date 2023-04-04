package com.example.feedcraft

import android.graphics.*
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.databinding.FragmentEditBinding
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.feedcraft.adapters.FilterAdapter
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

        UIApplication.tempBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,UIApplication.imageUri)
        gpuImage = GPUImage(context)
        gpuImage.setImage(UIApplication.tempBitmap)
        binding.imageViewToEdit.setDrawingCacheEnabled(true); //this thing so i can get a bitmap from imageViewToEdit
        Glide.with(requireActivity()).load(UIApplication.tempBitmap).into(binding.imageViewToEdit)

        binding.imageViewFinish.setOnClickListener{
            val action = EditFragmentDirections.actionEditFragmentToFinishFragment()
            findNavController().navigate(action)
        }

        binding.imageViewBack.setOnClickListener {
            activity?.finish()
        }


        viewModel.captionText.observe(viewLifecycleOwner) {
                caption -> binding.textViewCaption.text = caption
                val widthBitmap = binding.imageViewToEdit.getDrawingCache().width
                val heightBitmap= binding.imageViewToEdit.getDrawingCache().height
                val textBitmap = Bitmap.createBitmap(widthBitmap, heightBitmap , Bitmap.Config.ARGB_8888)
                val canvas = Canvas(textBitmap)
                val textPaint = Paint().apply {
                    color = Color.BLUE
                    textSize = 70f
                    style = Paint.Style.FILL
                    isAntiAlias = true
                }
                canvas.drawText(binding.textViewCaption.text.toString(), 70f, 70f, textPaint)

                // Merge the text bitmap with the original image using a GPUImageTwoInputFilter
                val blendFilter = GPUImageAddBlendFilter()
                blendFilter.bitmap = textBitmap
                gpuImage.setFilter(blendFilter)
                UIApplication.tempBitmap = gpuImage.bitmapWithFilterApplied
                binding.imageViewToEdit.setImageBitmap(gpuImage.bitmapWithFilterApplied)

        }

        binding.textViewCaption.setOnClickListener {
            val action = EditFragmentDirections.actionEditFragmentToAddCaptionDialogFragment()
            findNavController().navigate(action) }
        binding.ViewCaption.setOnClickListener{
            val action = EditFragmentDirections.actionEditFragmentToAddCaptionDialogFragment()
            findNavController().navigate(action) }

        binding.ViewFilter.setOnClickListener {
            recyclerView.isVisible = true
            binding.seekBar.isVisible = false
            percentageTextView.isVisible = false
        }

        binding.ViewBrightness.setOnClickListener {
            gpuImage.setImage(UIApplication.tempBitmap)
            openEditOptions()
            setBrightnessSelected()
            seekBar.setOnSeekBarChangeListener(null) //its important for this line to stay here and not two lines down below
            seekBar.progress = brightnessPercentage
            percentageTextView.text = "$brightnessPercentage%"
            val brightnessFilter = GPUImageBrightnessFilter()
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    percentageTextView.text = "$progress%"
                    brightnessPercentage = progress
                    val brightnessLevel = ((brightnessPercentage / 100.0f) * 2) - 1
                    brightnessFilter.setBrightness(brightnessLevel)
                    gpuImage.setFilter(brightnessFilter)
                    UIApplication.tempBitmap = gpuImage.bitmapWithFilterApplied
                    Glide.with(requireActivity()).load(gpuImage.bitmapWithFilterApplied).into(binding.imageViewToEdit)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }

        binding.ViewSaturation.setOnClickListener {
            gpuImage.setImage(UIApplication.tempBitmap)
            openEditOptions()
            setSaturationSelected()
            seekBar.setOnSeekBarChangeListener(null)
            seekBar.progress = saturationPercentage
            percentageTextView.text = "$saturationPercentage%"
            val saturationFilter = GPUImageSaturationFilter()
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    percentageTextView.text = "$progress%"
                    saturationPercentage = progress
                    saturationFilter.setSaturation(saturationPercentage/50.0f)
                    gpuImage.setFilter(saturationFilter)
                    UIApplication.tempBitmap = gpuImage.bitmapWithFilterApplied
                    Glide.with(requireActivity()).load(gpuImage.bitmapWithFilterApplied).into(binding.imageViewToEdit)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }

        binding.ViewContrast.setOnClickListener {
            gpuImage.setImage(UIApplication.tempBitmap)
            openEditOptions()
            setContrastSelected()
            seekBar.setOnSeekBarChangeListener(null)
            seekBar.progress = contrastPercentage
            percentageTextView.text = "$contrastPercentage%"
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    percentageTextView.text = "$progress%"
                    contrastPercentage = progress
                    gpuImage.setFilter(GPUImageContrastFilter((contrastPercentage/50f)))
                    UIApplication.tempBitmap = gpuImage.bitmapWithFilterApplied
                    Glide.with(requireActivity()).load(gpuImage.bitmapWithFilterApplied).into(binding.imageViewToEdit)
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
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        filterList = ArrayList()

        viewModel.addDataToList(requireActivity(), context, filterList)

        filterAdapter = FilterAdapter(filterList){ position ->
            UIApplication.tempBitmap = filterList[position].imageBitmap
            binding.imageViewToEdit.setImageBitmap(filterList[position].imageBitmap)
        }
        recyclerView.adapter = filterAdapter
    }


}