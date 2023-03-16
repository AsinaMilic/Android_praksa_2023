package com.example.feedcraft

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.feedcraft.databinding.FragmentFinishBinding
import java.io.File


class FinishFragment : Fragment() {

    private var _binding: FragmentFinishBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinishBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraOrGallery: String = requireActivity().intent?.extras?.getString("CameraOrGallery").toString()
        if(cameraOrGallery == "Gallery") {
            val selectedImageFromGalleryUri = UIApplication.imageUri
            Glide.with(requireActivity()).load(selectedImageFromGalleryUri).into(binding.imageViewFinishedImage)
        }
        else {
            val cameraImage = UIApplication.tempBitmap
            binding.imageViewFinishedImage.setImageBitmap(cameraImage)
        }

        binding.imageViewDiscard.setOnClickListener{
            activity?.finish()
        }
        binding.imageViewSave.setOnClickListener{
            val bitmap = UIApplication.tempBitmap
            if (bitmap != null)
                viewModel.saveBitmap(bitmap, context?.filesDir.toString() + File.separator + "saved_creations", "creation_1.png")

            activity?.finish()
        }
        binding.imageViewSchedule.setOnClickListener {
            val action = FinishFragmentDirections.actionFinishFragmentToScheduleReminderFragment()
            findNavController().navigate(action)
        }
        binding.imageViewShare.setOnClickListener {
            viewModel.shareImage(requireActivity().intent, context)
        }

    }


}