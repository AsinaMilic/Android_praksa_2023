package com.example.feedcraft

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment

class DeleteDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        val view = inflater.inflate(R.layout.fragment_delete_dialog, container, false)

        val layout = view?.findViewById<View>(R.id.delete_dialog)
        val childLayout = view?.findViewById<View>(R.id.viewFeedDialog)

        layout?.setOnClickListener {
            dismiss()
        }
        childLayout?.setOnClickListener {
            // Ovdje dodajte željenu akciju koju želite obaviti kada se klikne na unutarnji layout
            dismiss()
        }

        dialog?.setCanceledOnTouchOutside(true)

        return view
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

    }
}