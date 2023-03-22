package com.example.feedcraft

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels

class DeleteDialogFragment : DialogFragment() {

    private val viewModel: FeedViewModel by activityViewModels()

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
        val deleteDialog = view?.findViewById<View>(R.id.viewFeedDialog)
        val OKDel = view?.findViewById<TextView>(R.id.textViewDeleteOK)
        val CancelDel = view?.findViewById<TextView>(R.id.textViewDeleteCancel)

        layout?.setOnClickListener {
            dismiss()
        }
        deleteDialog?.setOnClickListener {
            OKDel?.setOnClickListener {
                viewModel.DeleteOrCancel(true)
            }
            CancelDel?.setOnClickListener {
                viewModel.DeleteOrCancel(false)
            }
            //dismiss()
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