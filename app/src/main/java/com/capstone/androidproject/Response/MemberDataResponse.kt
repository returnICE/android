package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class MemberDataResponse {

    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("memberdata")
    val memberdata:MemberData = MemberData()

}