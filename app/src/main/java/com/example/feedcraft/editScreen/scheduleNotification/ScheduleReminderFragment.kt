package com.example.feedcraft.editScreen.scheduleNotification

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.feedcraft.ReminderWorker
import com.example.feedcraft.editScreen.scheduleNotification.ScheduleReminderFragmentDirections
import com.example.feedcraft.UIApplication
import com.example.feedcraft.databinding.FragmentScheduleReminderBinding
import java.util.*
import java.util.concurrent.TimeUnit

class ScheduleReminderFragment : Fragment() {

    private var _binding: FragmentScheduleReminderBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScheduleReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Toast.makeText(context, "Just press OK when setting the time to get notification immediately",Toast.LENGTH_SHORT).show()

        binding.imageViewBack.setOnClickListener{
            val action =
                ScheduleReminderFragmentDirections.actionScheduleReminderFragmentToFinishFragment()
            findNavController().navigate(action)
        }

        val today = Calendar.getInstance()

        var chosenYear = today.get(Calendar.YEAR)
        var chosenMonth = today.get(Calendar.MONTH)
        var chosenDay = today.get(Calendar.DAY_OF_MONTH)
        var chosenHour = today.get(Calendar.HOUR)
        var chosenMin = today.get(Calendar.MINUTE)

        val descriptionText = binding.imageViewScheduleMessage
        val btnDone = binding.imageViewDone
        val datePicker = binding.imageViewDate
        val timePicker = binding.textViewTime

        datePicker.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1,
                    { _, year, month, dayOfMonth ->
                        chosenYear = year
                        chosenMonth = month
                        chosenDay = dayOfMonth
                        datePicker.text = "$dayOfMonth/${month + 1}/$year"
                    },
                    chosenYear,
                    chosenMonth,
                    chosenDay
                )
            }?.show()
        }

        timePicker.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    chosenHour = hourOfDay
                    chosenMin = minute
                    timePicker.text = String.format("%02d:%02d", chosenHour, chosenMin)
                },
                chosenHour,
                chosenMin,
                true
            )
            timePickerDialog.show()
        }
// 5
        btnDone.setOnClickListener {
            // 6 Get the DateTime the user selected
            val userSelectedDateTime = Calendar.getInstance()
            userSelectedDateTime.set(chosenYear, chosenMonth, chosenDay, chosenHour , chosenMin)
            val todayDateTime = Calendar.getInstance()
            val delayInSeconds = (userSelectedDateTime.timeInMillis/1000L) - (todayDateTime.timeInMillis/1000L)
            createWorkRequest(descriptionText.text.toString(), delayInSeconds)
            Toast.makeText(requireContext(), "Reminder set", Toast.LENGTH_SHORT).show()
        }

        binding.checkboxChecked.setOnCheckedChangeListener  { _, isChecked ->
            UIApplication.addPictureInNotification = isChecked
        }

    }

    private fun createWorkRequest(message: String,timeDelayInSeconds: Long  ) {
        val myWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
            .setInputData(workDataOf("title" to "Reminder", "message" to message,))
            .build()
        WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)
    }

}