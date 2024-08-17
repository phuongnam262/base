package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class KioskConfig(
    @SerializedName("home_screen_logo") val homeScreenLogo: String,
    @SerializedName("sub_screen_logo") val subScreenLogo: String,
    @SerializedName("home_screen_background") val homeScreenBackground: String,
    @SerializedName("sub_screen_background") val subScreenBackground: String,
    @SerializedName("owner_text") val ownerText: String,
    @SerializedName("owner_image") val ownerImage: String,
    @SerializedName("email_domain") val emailDomain: String,
    @SerializedName("administrator_support_email") val administratorSupportEmail: String,
    @SerializedName("order_category_on_kiosk") val orderCategoryOnKiosk: String,
    @SerializedName("order_model_on_kiosk") val orderModelOnKiosk: String,
    @SerializedName("enable_internal_delivery") val enableInternalDelivery: Boolean,
    @SerializedName("auto_trigger_delay") val autoTriggerDelay: String,
    @SerializedName("auto_next_page") val autoNextPage: String,
    @SerializedName("check_door_status_on_confirm") val checkDoorStatusOnConfirm: Boolean,
    @SerializedName("mandatory_door_closure_verification") val mandatoryDoorClosureVerification: Boolean
)
