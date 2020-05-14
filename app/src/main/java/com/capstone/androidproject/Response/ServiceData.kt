package com.capstone.androidproject.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServiceData(
    @SerializedName("endDate")
    val endDate: String = "",

    @SerializedName("term")
    val term: Int = 0,

    @SerializedName("limitTimes")
    val limitTimes: Int = 0,

    @SerializedName("usedTimes")
    val usedTimes: Int = 0,

    @SerializedName("subId")
    val subId: Int = 0,

    @SerializedName("subName")
    val subName: String = "",

    @SerializedName("sellerId")
    val sellerId: String = "",

    @SerializedName("price")
    val price: Int = 0,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("menu")
    val menu:ArrayList<MenuData> = ArrayList()

):Parcelable