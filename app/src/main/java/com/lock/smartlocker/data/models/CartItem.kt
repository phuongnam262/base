package com.lock.smartlocker.data.models

import android.os.Parcel
import android.os.Parcelable

data class CartItem(
    val model: AvailableModel,
    val category: AvailableCategory,
    var quantity: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(AvailableModel::class.java.classLoader)!!,
        parcel.readParcelable(AvailableCategory::class.java.classLoader)!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(model, flags)
        parcel.writeParcelable(category, flags)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }
}
