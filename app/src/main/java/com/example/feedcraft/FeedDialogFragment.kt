package com.example.feedcraft

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment

class FeedDialogFragment : DialogFragment() {
    //private var _binding: FragmentEditBinding? = null
    //private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view = inflater.inflate(R.layout.fragment_feed_dialog, container, false)

        val parentLayout = view?.findViewById<View>(R.id.parent_feed_dialog)
        val childLayout = view?.findViewById<View>(R.id.child_feed_dialog)
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
    /*override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dismiss()
    }*/

}