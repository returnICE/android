package com.capstone.androidproject.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EatenLogData (
    @SerializedName("eatenId")
    val eatenId: Int = 0,

    @SerializedName("eatenDate")
    val eatenDate: String = "",

    @SerializedName("score")
    val score: Int? = 0,

    @SerializedName("enterpriseId")
    val enterpriseId: String? = "",

    @SerializedName("menuName")
    val menuName: String = "",

    @SerializedName("price")
    val price: Int = 0

): Parcelable