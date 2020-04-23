package com.capstone.androidproject.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MenuData (
    @SerializedName("menuId")
    val menuId: Int = 0,

    @SerializedName("menuName")
    val menuName: String = "",

    @SerializedName("price")
    val price: Int = 0,

    @SerializedName("avgScore")
    val avgScore: Double = 0.0

):Parcelable