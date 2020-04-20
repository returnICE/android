package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

data class UserData (

        @SerializedName("customerId")
        val customerId: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("phone")
        val phone: String = "",

        @SerializedName("birth")
        val birth: String = ""
)