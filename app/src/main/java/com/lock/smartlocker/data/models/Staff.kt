package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class Staff(
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("mobile_number")
    val mobileNumber: String,
    val email: String,
    @SerializedName("user_type")
    val userType: Int,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("is_otp")
    val isOtp: Boolean,
    @SerializedName("user_token")
    val userToken: String,
    @SerializedName("is_admin_console")
    val isAdminConsole: Boolean,
    @SerializedName("is_topup_items")
    val isTopupItems: Boolean,
    @SerializedName("is_retrieve_items")
    val isRetrieveItems: Boolean,
    @SerializedName("is_retrieve_faulty")
    val isRetrieveFaulty: Boolean,
    @SerializedName("is_end_reservation")
    val isEndReservation: Boolean,
    @SerializedName("is_manage_lockers")
    val isManageLockers: Boolean,
    @SerializedName("is_elocker_settings")
    val isElockSettings: Boolean,
    @SerializedName("is_close_application")
    val isCloseApplication: Boolean,
    @SerializedName("is_manage_media")
    val isManageMedia: Boolean
)
