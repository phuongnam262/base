package com.lock.smartlocker.util.view.custom

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.lock.smartlocker.R

class ReportStockDialog : DialogFragment() {

    interface ReportStockDialogListener {
        fun onDialogSubmit(reason: String, lockerId: String, consumableId: String)
        fun onDialogCancel()
    }

    private var listener: ReportStockDialogListener? = null
    private var lockerId: String? = null
    private var consumableId: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as? ReportStockDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((parentFragment ?: context).toString() + " must implement ReportStockDialogListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lockerId = it.getString("lockerId")
            consumableId = it.getString("consumableId")
        }
    }

    @SuppressLint("UseGetLayoutInflater", "MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_report_stock, null)

        val spinner = view.findViewById<Spinner>(R.id.spinner_stock_options)
        val cancelButton = view.findViewById<Button>(R.id.btn_cancel)
        val submitButton = view.findViewById<Button>(R.id.btn_submit)

        cancelButton.setOnClickListener {
            listener?.onDialogCancel()
            dismiss()
        }
        submitButton.setOnClickListener {
            val selectedOption = spinner.selectedItem.toString()
            listener?.onDialogSubmit(selectedOption, lockerId ?: "", consumableId ?: "")
            dismiss()
        }

        return Dialog(requireContext()).apply {
            setContentView(view)
        }
    }

    companion object {
        fun newInstance(lockerId: String, consumableId: String): ReportStockDialog {
            val fragment = ReportStockDialog()
            val args = Bundle()
            args.putString("lockerId", lockerId)
            args.putString("consumableId", consumableId)
            fragment.arguments = args
            return fragment
        }
    }
}