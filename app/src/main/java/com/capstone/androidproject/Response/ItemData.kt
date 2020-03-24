package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

data class ItemData (
    @SerializedName("manufactdate")
    val manufactdate: String = "",

    @SerializedName("expirationdate")
    val expirationdate: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("information")
    val information: String = "",

    @SerializedName("originprice")
    val originprice : String = "",

    @SerializedName("saleprice")
    val saleprice : String = ""
)