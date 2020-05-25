package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class EatenLogDataResponse2 {

    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("data")
    val data:List<EatenLogData> = ArrayList()

}