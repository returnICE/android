package com.capstone.androidproject.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Campaign (

    @SerializedName("sellerId")
    val sellerId: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("body")
    val body: String = "",

    @SerializedName("transmitDate")
    val transmitDate: String = "",

    @SerializedName("Seller")
    val seller: SellerData = SellerData()

): Parcelable