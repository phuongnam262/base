package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class Locker(
    @SerializedName("locker_id") val lockerId: String,
    @SerializedName("name") val name: String,
    @SerializedName("hardware_address") val hardwareAddress: Int,
    @SerializedName("size_id") val sizeId: Int,
    @SerializedName("locker_size_key") val lockerSizeKey: String,
    @SerializedName("position") val position: Int,
    @SerializedName("width") val width: Double,
    @SerializedName("height") val height: Double,
    @SerializedName("group") val group: Int,
    @SerializedName("type") val type: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("board_address") val boardAddress: Int,
    @SerializedName("block_name") val blockName: String,
    @SerializedName("locker_action") val lockerAction: Int,
    @SerializedName("locker_status") val lockerStatus: Int,
    @SerializedName("item_detect") val itemDetect: Boolean,
    @SerializedName("block_collect_item") val blockCollectItem: Boolean
)