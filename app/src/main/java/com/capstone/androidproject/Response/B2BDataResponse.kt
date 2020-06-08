package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class B2BDataResponse {
    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("b2bdata")
    val b2bdata:List<B2BData> = ArrayList()
}