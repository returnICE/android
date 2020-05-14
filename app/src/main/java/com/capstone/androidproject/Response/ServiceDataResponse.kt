package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class ServiceDataResponse {
    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("subedItem")
    val subedItem:List<ServiceData> = ArrayList()
}