package com.lock.smartlocker.util.view.custom

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
        fun onDialogPositiveClick(dialogTag: String?)
        fun onDialogNegativeClick(dialogTag: String?)
    }

    private var listener: ConfirmationDialogListener? = null
    private var message: String? = null
    private var positiveButtonText: String? = null
    private var negativeButtonText: String? = null
    private var dialogTag: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as? ConfirmationDialogListener // Lấy listener từ Fragment cha
        } catch (e: ClassCastException) {
            throw ClassCastException((parentFragment ?: context).toString() + " must implement ConfirmationDialogListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message = it.getString("message")
            positiveButtonText = it.getString("positiveButtonText")
            negativeButtonText = it.getString("negativeButtonText")
            dialogTag = it.getString("dialogTag")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm, null)

        val messageTextView = view.findViewById<TextView>(R.id.tv_message)
        val positiveButton = view.findViewById<Button>(R.id.btn_confirm)
        val negativeButton = view.findViewById<Button>(R.id.btn_cancel)

        messageTextView.text = message
        positiveButton.text = positiveButtonText ?: "Yes" // Giá trị mặc định nếu positiveButtonText là null
        negativeButton.text = negativeButtonText ?: "No" // Giá trị mặc định nếu negativeButtonText là null

        positiveButton.setOnClickListener {
            listener?.onDialogPositiveClick(dialogTag)
            dismiss()
        }
        negativeButton.setOnClickListener {
            listener?.onDialogNegativeClick(dialogTag)
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    companion object {
        fun newInstance(
            title: String? = null, // Cho phép title là null
            message: String,
            positiveButtonText: String? = null,
            negativeButtonText: String? = null,
            dialogTag: String? = null
        ): CustomConfirmDialog {
            val fragment = CustomConfirmDialog()
            val args = Bundle()
            args.putString("message", message)
            if (positiveButtonText != null) {
                args.putString("positiveButtonText", positiveButtonText)
            }
            if (negativeButtonText != null) {
                args.putString("negativeButtonText", negativeButtonText)
            }
            args.putString("dialogTag", dialogTag)
            fragment.arguments = args
            return fragment
        }
    }
}