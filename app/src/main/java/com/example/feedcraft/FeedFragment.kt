package com.example.feedcraft

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController

import com.example.feedcraft.databinding.FragmentFeedBinding


class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFeedBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewDelete.setOnClickListener{
            val action = FeedFragmentDirections.actionItemFeedToDeleteDialogFragment()
            findNavController().navigate(action)
        }
        binding.textViewEdit.setOnClickListener{
            val action = FeedFragmentDirections.actionItemFeedToEditFragment()
            findNavController().navigate(action)
        }
        binding.imageViewButtonPlus.setOnClickListener{
            val action = FeedFragmentDirections.actionItemFeedToFeedDialogFragment()
            findNavController().navigate(action)
        }
    }




}