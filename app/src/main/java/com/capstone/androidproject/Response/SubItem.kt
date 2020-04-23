package com.capstone.androidproject.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubItem (
    @SerializedName("subId")
    val subId: Int = 0,

    @SerializedName("subName")
    val subName: String = "",

    @SerializedName("price")
    val price: Int = 0,

    @SerializedName("limitTimes")
    val limitTimes: Int = 0,

    @SerializedName("term")
    val term: Int = 0,

    @SerializedName("subs")
    val subs : Int = 0,

    @SerializedName("info")
    val info: String? = "",

    @SerializedName("menu")
    val menu:ArrayList<MenuData> = ArrayList()

):Parcelable