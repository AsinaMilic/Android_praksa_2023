package com.example.feedcraft

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController

import androidx.navigation.ui.setupWithNavController
import com.example.feedcraft.databinding.FragmentEditBinding


class EditFragment : Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewDelete.setOnClickListener{
            val action = EditFragmentDirections.actionItemFeedToDeleteDialogFragment()
            findNavController().navigate(action)
        }
        /*binding.textViewEdit.setOnClickListener{
            val action = EditFragmentDirections.actionItemFeedToFeedDialogFragment()
            findNavController().navigate(action)
        }*/
    }

}