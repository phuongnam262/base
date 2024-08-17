package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class Terminal(
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: Int,
    @SerializedName("terminal_id") val terminalId: String,
    @SerializedName("item_detector") val itemDetector: Boolean,
    @SerializedName("is_update_now") val isUpdateNow: Boolean,
    @SerializedName("url_file_update") val urlFileUpdate: String,
    @SerializedName("boards") val boards: ArrayList<Board>
)