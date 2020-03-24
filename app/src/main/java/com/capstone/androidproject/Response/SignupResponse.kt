package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class SignupResponse {

    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("user")
    val user = UserData()
}