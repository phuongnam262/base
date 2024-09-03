package com.lock.smartlocker.util.view.custom

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.lock.smartlocker.R

class CustomConfirmDialog : DialogFragment() {

    interface ConfirmationDialogListener {
        fun onDialogConfirmClick(dialogTag: String?)
        fun onDialogCancelClick()
    }

    private var listener: ConfirmationDialogListener? = null
    private var message: String? = null
    private var dialogTag: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as? ConfirmationDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((parentFragment ?: context).toString() + " must implement ConfirmationDialogListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message = it.getString("message")
            dialogTag = it.getString("dialogTag")

        }
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm, null)

        val messageTextView = view.findViewById<TextView>(R.id.tv_message)
        val confirmButton = view.findViewById<Button>(R.id.btn_confirm)
        val cancelButton = view.findViewById<Button>(R.id.btn_cancel)

        messageTextView.text = message

        confirmButton.setOnClickListener {
            listener?.onDialogConfirmClick(dialogTag)
            dismiss()
        }
        cancelButton.setOnClickListener {
            listener?.onDialogCancelClick()
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    companion object {
        fun newInstance(
            message: String,
            dialogTag: String? = null
        ): CustomConfirmDialog {
            val fragment = CustomConfirmDialog()
            val args = Bundle()
            args.putString("message", message)
            args.putString("dialogTag", dialogTag)
            fragment.arguments = args
            return fragment
        }
    }
}