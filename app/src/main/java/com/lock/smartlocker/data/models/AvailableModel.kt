package com.lock.smartlocker.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AvailableModel(
    @SerializedName("model_id") val modelId: String,
    @SerializedName("loanable") val loanable: Int?,
    @SerializedName("available") val available: Int,
    @SerializedName("model_image") val modelImage: String,
    @SerializedName("model_name") val modelName: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(modelId)
        parcel.writeValue(loanable)
        parcel.writeInt(available)
        parcel.writeString(modelImage)
        parcel.writeString(modelName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AvailableModel> {
        override fun createFromParcel(parcel: Parcel): AvailableModel {
            return AvailableModel(parcel)
        }

        override fun newArray(size: Int): Array<AvailableModel?> {
            return arrayOfNulls(size)
        }
    }
}