package com.example.feedcraft.editScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.feedcraft.editScreen.FinishFragmentDirections
import com.example.feedcraft.UIApplication
import com.example.feedcraft.databinding.FragmentFinishBinding
import com.example.feedcraft.viewModels.EditViewModel


class FinishFragment : Fragment() {

    private lateinit var binding: FragmentFinishBinding
    private val viewModel: EditViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireActivity()).load(UIApplication.tempBitmap).into(binding.imageViewFinishedImage)

        viewModel.captionText.observe(viewLifecycleOwner) { caption -> binding.textViewCaptionFinish.text = caption }

        binding.imageViewDiscard.setOnClickListener{ activity?.finish() }

        binding.imageViewSave.setOnClickListener{
            viewModel.saveEditedPicture(requireContext())
            activity?.finish()
        }
        binding.imageViewSchedule.setOnClickListener {
            val action = FinishFragmentDirections.actionFinishFragmentToScheduleReminderFragment()
            findNavController().navigate(action)
        }
        binding.imageViewShare.setOnClickListener {
            viewModel.shareImage(context)
        }

    }

}