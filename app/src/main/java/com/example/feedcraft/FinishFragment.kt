package com.example.feedcraft

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.databinding.ActivityMainBinding
import com.example.feedcraft.databinding.FragmentFinishBinding
import com.example.feedcraft.databinding.FragmentHomeBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


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

        /*viewModel.bitmap.observe(viewLifecycleOwner){ newBitmap->
            Toast.makeText(requireContext(), "Stigla je bitmapa u newBitmap?", Toast.LENGTH_LONG).show()
            val a = 1
            val b = 3
        }*/

        val cameraImage = UIApplication.editedImage?.bitmap

        binding.imageViewDiscard.setOnClickListener{
            //val action = FinishFragmentDirections.actionFinishFragmentToItemFeed()
            //findNavController().navigate(action)
            activity?.finish()

        }
        binding.imageViewSave.setOnClickListener{
            val bitmap = UIApplication.editedImage?.bitmap

            if (bitmap != null)
                viewModel.saveBitmap(bitmap, context?.filesDir.toString(), "kao fajl")


            activity?.finish()
        }
        binding.imageViewSchedule.setOnClickListener {
            val action = FinishFragmentDirections.actionFinishFragmentToScheduleReminderFragment()
            findNavController().navigate(action)
        }


    }

}