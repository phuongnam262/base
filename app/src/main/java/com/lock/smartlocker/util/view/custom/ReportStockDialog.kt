package com.lock.smartlocker.util.view.custom

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
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
    private val handler = Handler(Looper.getMainLooper())
    private var dismissRunnable: Runnable? = null

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

        val spinner = view.findViewById<Spinner>(R.id.spi_report)
        val cancelButton = view.findViewById<Button>(R.id.btn_cancel)
        val submitButton = view.findViewById<Button>(R.id.btn_submit)
        val closeButton = view.findViewById<Button>(R.id.btn_close)
        val tvMessage = view.findViewById<TextView>(R.id.tv_message)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.stock_options,
            R.layout.item_spinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.setSelection(0)

        cancelButton.setOnClickListener {
            listener?.onDialogCancel()
            dismiss()
        }
        closeButton.setOnClickListener {
            listener?.onDialogCancel()
            dismissRunnable?.let { handler.removeCallbacks(it) }
            dismiss()
        }
        submitButton.setOnClickListener {
            val selectedReason = if(spinner.selectedItemPosition == 0) "Found less stock" else "Found extra stock"
            listener?.onDialogSubmit(selectedReason , lockerId ?: "", consumableId ?: "")
            cancelButton.visibility = View.GONE
            submitButton.visibility = View.GONE
            closeButton.visibility = View.VISIBLE
            tvMessage.visibility = View.VISIBLE
            dismissRunnable = Runnable {
                dismiss()
            }
            handler.postDelayed(dismissRunnable!!, 2000)
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