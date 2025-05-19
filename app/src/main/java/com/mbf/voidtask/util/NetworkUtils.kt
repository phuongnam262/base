package com.lock.basesource.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * Handling checking network
 */
object NetworkUtils {

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        return false
    }
}