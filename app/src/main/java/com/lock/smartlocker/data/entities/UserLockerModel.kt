package com.lock.smartlocker.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserLockerModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var personName: String? = null,
    var personCode: String? = null,
    var personGroup: String? = null,
    var status: Int? = null,
    var email: String? = null,
    var isDelete: Int? = null,
    var faceBase64: String? = null
)