package com.lock.basesource.util

import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.lock.basesource.R


object CommonUtils {

    private var isDialogShowing = false
    /**
     * Showing snacknar by ResourceId
     */
    fun showSnackbar(
        view: View?,
        resId: Int
    ) {
        view?.let { Snackbar.make(it, resId, Snackbar.LENGTH_LONG).show() }
    }

    /**
     * Showing snackbar by message string
     */
    fun showSnackbar(
        view: View?,
        message: String,
        isAnchorView: Boolean = false
    ) {
        if (message.isEmpty()) return
        view?.let {
            val snackbar = Snackbar.make(
                it, message,
                Snackbar.LENGTH_LONG
            )
            if (isAnchorView) snackbar.anchorView = it
            snackbar.show()
        }
    }

    /**
     * Showing error dialog with message and OK button
     */
    fun showErrorDialog(
        context: Context?,
        title: String = "",
        message: String,
        theme: Int = androidx.appcompat.R.style.Base_AlertDialog_AppCompat_Light
    ) {
        if (message.isEmpty() || isDialogShowing) return
        context?.let {
            isDialogShowing = true
            MaterialAlertDialogBuilder(it, theme)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                    it.resources.getString(R.string.close)
                ) { dialog, _ ->
                    dialog.dismiss()
                    isDialogShowing = false
                }
                .setOnDismissListener {
                    isDialogShowing = false
                }
                .show()
        }
    }

    /**
     * Processing start a activity
     * className: activity will be started
     */
    fun <T> startActivity(context: Context?, className: Class<T>) {
        context?.let {
            val intent = Intent(context, className)
            it.startActivity(intent)
        }
    }

}