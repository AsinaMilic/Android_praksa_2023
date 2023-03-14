package com.example.feedcraft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.databinding.FragmentEditBinding
import androidx.fragment.app.FragmentManager

/*import com.example.feedcraft.databinding.FragmentEditBinding*/



class EditFragment :  Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            /*val action = EditFragmentDirections.
            findNavController().navigate(action)*/
            // Get the fragment instance

        }


    }
}