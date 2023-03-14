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
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.databinding.FragmentEditBinding
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide

/*import com.example.feedcraft.databinding.FragmentEditBinding*/


class EditFragment :  Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val a = requireActivity().intent
        //val intent = getIntent()?.extras?.getString("letter")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ViewCaption.setOnClickListener{
            val action = EditFragmentDirections.actionEditFragmentToAddCaptionDialogFragment()
            findNavController().navigate(action)
        }
        binding.imageViewFinish.setOnClickListener{
            val action = EditFragmentDirections.actionEditFragmentToFinishFragment()
            findNavController().navigate(action)
        }
        binding.imageViewBack.setOnClickListener {

            //val intent = Intent(requireContext(), MainActivity::class.java)
            //requireActivity().startActivity(intent)
            //((requireActivity().application) as UIApplication).tempBitmap = null
            activity?.finish()

        }


        val cameraOrGallery: String = requireActivity().intent?.extras?.getString("CameraOrGallery").toString()

        if(cameraOrGallery == "Gallery") {
            val selectedImageFromGalleryUri = ((requireActivity().application) as UIApplication).imageUri
            Glide.with(requireActivity()).load(selectedImageFromGalleryUri).into(binding.imageViewToEdit)
        }
        else {
            val cameraImage = ((requireActivity().application) as UIApplication).tempBitmap
            //Glide.with(requireActivity()).load(cameraImage).into(binding.imageViewToEdit)
            binding.imageViewToEdit.setImageBitmap(cameraImage)
        }

    }

}