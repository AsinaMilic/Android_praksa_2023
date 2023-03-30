package com.example.feedcraft

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.feedcraft.databinding.FragmentFinishBinding
import java.io.File
import java.io.InputStream


class FinishFragment : Fragment() {

    private var _binding: FragmentFinishBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFinishBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireActivity()).load(UIApplication.tempBitmap).into(binding.imageViewFinishedImage)

        viewModel.captionText.observe(viewLifecycleOwner) { caption -> binding.textViewCaptionFinish.text = caption }

        binding.imageViewDiscard.setOnClickListener{ activity?.finish() }

        binding.imageViewSave.setOnClickListener{
            val firstPath: String = context?.filesDir.toString() + File.separator
            val fileName: String = System.currentTimeMillis().toString()

            //val uri: Uri? = UIApplication.imageUri
            //val inputStream: InputStream? = uri?.let { it1 -> context?.contentResolver?.openInputStream(it1) }
            //val bitmap = BitmapFactory.decodeStream(inputStream)
            val bitmap = UIApplication.tempBitmap
            if (bitmap != null) {
                viewModel.saveBitmap(bitmap, firstPath + "saved_creations", fileName)
                viewModel.saveBitmapPreview(bitmap, firstPath + "creation_previews",300, fileName)
            }
            UIApplication.imageUri = null
            UIApplication.tempBitmap = null
            UIApplication.galleryListChanged = true
            viewModel.save2SharedPreferences(requireContext(),"hardcoded_caption", fileName)
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