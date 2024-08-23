package com.lock.smartlocker.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class LockerInfo(
    @SerializedName("locker_id") val lockerId: String,
    @SerializedName("locker_name") val lockerName: String,
    @SerializedName("serial_number") val serialNumber: String,
    @SerializedName("model_name") val modelName: String,
    @SerializedName("model_id") val modelId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("door_status") var doorStatus: Int,
    var quantity : Int = 1,
    var scanStatus : String,
    var scanValue : Int = 0,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lockerId)
        parcel.writeString(lockerName)
        parcel.writeString(serialNumber)
        parcel.writeString(modelName)
        parcel.writeString(modelId)
        parcel.writeString(categoryName)
        parcel.writeInt(doorStatus)
        parcel.writeInt(quantity)
        parcel.writeString(scanStatus)
        parcel.writeInt(scanValue)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LockerInfo> {
        override fun createFromParcel(parcel: Parcel): LockerInfo {
            return LockerInfo(parcel)
        }

        override fun newArray(size: Int): Array<LockerInfo?> {
            return arrayOfNulls(size)
        }
    }
}