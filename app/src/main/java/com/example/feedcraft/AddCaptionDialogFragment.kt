package com.example.feedcraft

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment


class AddCaptionDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view = inflater.inflate(R.layout.fragment_add_caption_dialog, container, false)

        val parentLayout = view?.findViewById<View>(R.id.parent_add_caption_dialog)
        val childLayout = view?.findViewById<View>(R.id.child_add_caption_dialog)
        parentLayout?.setOnClickListener {
            dismiss()
        }
        childLayout?.setOnClickListener {
            // Ovdje dodajte željenu akciju koju želite obaviti kada se klikne na unutarnji layout
            dismiss()
        }
        /*dialog?.setCancelable(false)*/
        dialog?.setCanceledOnTouchOutside(true)

        return view
    }

}