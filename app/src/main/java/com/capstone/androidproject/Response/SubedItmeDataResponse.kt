package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class SubedItmeDataResponse {
    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("subdata")
    val subdata:List<SubedItemData> = ArrayList()
}