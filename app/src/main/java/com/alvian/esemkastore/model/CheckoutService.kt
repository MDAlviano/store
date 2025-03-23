package com.alvian.esemkastore.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONArray
import java.util.ArrayList

data class CheckoutService(
    val id: Int,
    val name: String,
    val duration: Int,
    val price: Long,
    val transaction: JSONArray
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readLong(),
        TODO("transaction")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeInt(duration)
        parcel.writeLong(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CheckoutService> {
        override fun createFromParcel(parcel: Parcel): CheckoutService {
            return CheckoutService(parcel)
        }

        override fun newArray(size: Int): Array<CheckoutService?> {
            return arrayOfNulls(size)
        }
    }
}
