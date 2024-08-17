package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class Board(
    @SerializedName("board") val board: Int,
    @SerializedName("number_locker") val numberLocker: Int
)
