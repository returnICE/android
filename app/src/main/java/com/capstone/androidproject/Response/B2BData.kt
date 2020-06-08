package com.capstone.androidproject.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class B2BData(
    @SerializedName("sellerId")
    val sellerId: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("minPrice")
    val minPrice: Int = 0,

    @SerializedName("imgURL")
    val imgURL: String = ""


) : Parcelable