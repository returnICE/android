package com.capstone.androidproject.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubedItemData (
    @SerializedName("customerId")
    val customerId: String = "",

    @SerializedName("subId")
    val subId: String = "",

    @SerializedName("startDate")
    val startDate: String = "",

    @SerializedName("endDate")
    val endDate: String = "",

    @SerializedName("term")
    val term: Int = 0,

    @SerializedName("limitTimes")
    val limitTimes: Int = 0,

    @SerializedName("autoPay")
    val autoPay: Int = 0,

    @SerializedName("usedTimes")
    val usedTimes: Int = 0,

    @SerializedName("subedId")
    val subedId: String = ""
):Parcelable