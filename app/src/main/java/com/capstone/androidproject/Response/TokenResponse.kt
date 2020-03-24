package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class TokenResponse {

    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("token")
    val token:String =""
}