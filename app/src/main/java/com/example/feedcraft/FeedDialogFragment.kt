package com.example.feedcraft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment

class FeedDialogFragment : DialogFragment() {
    //private var _binding: FragmentEditBinding? = null
    //private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.full_screen_dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view = inflater.inflate(R.layout.fragment_feed_dialog, container, false)

        val parentLayout = view?.findViewById<View>(R.id.parent_feed_dialog)
       // val childLayout = view?.findViewById<View>(R.id.child_feed_dialog)
        parentLayout?.setOnClickListener {
            dismiss()
        }
        /*childLayout?.setOnClickListener {
            // Ovdje dodajte željenu akciju koju želite obaviti kada se klikne na unutarnji layout
            dismiss()
        }*/
        /*dialog?.setCancelable(false)*/
        dialog?.setCanceledOnTouchOutside(true)

        return view
    }
    /*override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dismiss()
    }*/


    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

    }

}