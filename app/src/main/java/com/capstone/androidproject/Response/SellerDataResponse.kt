package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class SellerDataResponse {

    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("sellerdata")
    val sellerdata:List<SellerData> = ArrayList()
}