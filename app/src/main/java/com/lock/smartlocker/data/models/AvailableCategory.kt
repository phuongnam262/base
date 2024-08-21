package com.lock.smartlocker.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class AvailableCategory(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("loanable") val loanable: Int?,
    @SerializedName("models") val models: List<AvailableModel>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(AvailableModel.CREATOR)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryId)
        parcel.writeString(categoryName)
        parcel.writeValue(loanable)

        parcel.writeTypedList(models)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AvailableCategory> {
        override fun createFromParcel(parcel: Parcel): AvailableCategory {
            return AvailableCategory(parcel)
        }

        override fun newArray(size: Int): Array<AvailableCategory?> {
            return arrayOfNulls(size)
        }
    }
}