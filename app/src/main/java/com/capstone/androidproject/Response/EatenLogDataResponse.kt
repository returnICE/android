package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class EatenLogDataResponse {

    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("data")
    val data:EatenLogData = EatenLogData()
}