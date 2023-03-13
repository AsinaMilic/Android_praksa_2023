package com.example.feedcraft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.databinding.ActivityMainBinding
import com.example.feedcraft.databinding.FragmentFinishBinding
import com.example.feedcraft.databinding.FragmentHomeBinding


class FinishFragment : Fragment() {

    private var _binding: FragmentFinishBinding? = null
    private val binding get() = _binding!!
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

        binding.imageViewDiscard.setOnClickListener{
            val action = FinishFragmentDirections.actionFinishFragmentToItemFeed()
            findNavController().navigate(action)

        }
        binding.imageViewSave.setOnClickListener{
            val action = FinishFragmentDirections.actionFinishFragmentToItemFeed()
            findNavController().navigate(action)

        }
        binding.imageViewSchedule.setOnClickListener {
            val action = FinishFragmentDirections.actionFinishFragmentToScheduleReminderFragment()
            findNavController().navigate(action)
        }


    }

}