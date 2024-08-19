package com.lock.smartlocker.data.entities.request

import com.google.gson.annotations.SerializedName

data class HardwareControllerRequest(
    @SerializedName("locker_ids") var lockerIds: List<String>? = null,
    @SerializedName("user_handler") var userHandler: String? = null,
    @SerializedName("open_type") var openType: String? = null
)

//class HardwareControllerRequest {
//    var locker_ids: List<String>? = null
//    var user_handler : String? = null
//    var open_type: String? = null
//    constructor(
//        locker_ids: List<String>?,
//        user_handler: String?,
//        open_type: String?
//    ) {
//        this.locker_ids = locker_ids
//        this.user_handler = user_handler
//        this.open_type = open_type
//    }
//}
