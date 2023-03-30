package com.example.feedcraft

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController


class AddCaptionDialogFragment : DialogFragment() {
    private val viewModel: EditViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val view = inflater.inflate(R.layout.fragment_add_caption_dialog, container, false)

        val parentLayout = view?.findViewById<View>(R.id.add_caption_dialog)
        val childLayout = view?.findViewById<View>(R.id.viewAddCaptionDialog)
        val textDialog = view?.findViewById<TextView>(R.id.textViewDialogCaption)
        val okBtn = view?.findViewById<TextView>(R.id.textViewOK)
        val cancelBtn = view?.findViewById<TextView>(R.id.textViewCancelCaption)

        parentLayout?.setOnClickListener {
            dismiss()
        }

        /*textDialog?.setOnClickListener {

        }*/
        okBtn?.setOnClickListener {
            viewModel.setCaptionText(textDialog?.text.toString())
            findNavController().navigateUp()
        }
        cancelBtn?.setOnClickListener {
            findNavController().navigateUp()
        }
        /*childLayout?.setOnClickListener {
            //dismiss()
        }*/

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