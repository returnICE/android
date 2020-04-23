package com.capstone.androidproject.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SellerData (
    @SerializedName("sellerId")
    val sellerId: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("phone")
    val phone: String = "",

    @SerializedName("address")
    val address: String = "",

    @SerializedName("totalSubs")
    val totalSubs: Int = 0,

    @SerializedName("lat")
    val lat: Double = 0.0,

    @SerializedName("lon")
    val lon: Double = 0.0,

    @SerializedName("distance")
    val distance: Double = 0.0,

    @SerializedName("imgURL")
    val imgURL : String? = "",

    @SerializedName("info")
    val info : String? = "",

    @SerializedName("type")
    val type : String = "",

    @SerializedName("minPrice")
    val minPrice : Int = 0
):Parcelable