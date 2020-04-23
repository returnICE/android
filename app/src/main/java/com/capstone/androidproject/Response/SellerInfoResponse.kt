package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class SellerInfoResponse {

    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("sellerdetaildata")
    val sellerdetaildata:SellerData = SellerData()

    @SerializedName("menudata")
    val menudata:ArrayList<MenuData> = ArrayList()

    @SerializedName("subItem")
    val subItem:ArrayList<SubItem> = ArrayList()
}