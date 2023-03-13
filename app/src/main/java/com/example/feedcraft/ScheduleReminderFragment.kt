package com.example.feedcraft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.databinding.FragmentEditBinding
import com.example.feedcraft.databinding.FragmentScheduleReminderBinding

class ScheduleReminderFragment : Fragment() {

    private var _binding: FragmentScheduleReminderBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScheduleReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewBack.setOnClickListener{
            val action = ScheduleReminderFragmentDirections.actionScheduleReminderFragmentToFinishFragment()
            findNavController().navigate(action)
        }

        binding.imageViewDone.setOnClickListener{
            val action = ScheduleReminderFragmentDirections.actionScheduleReminderFragmentToFinishFragment()
            findNavController().navigate(action)
        }


    }

}