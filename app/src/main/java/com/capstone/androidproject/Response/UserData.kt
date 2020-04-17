package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

data class UserData (

        @SerializedName("ID")
        val ID: String = "",

        @SerializedName("PW")
        val PW: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("phone")
        val phone: String = "",

        @SerializedName("birth")
        val birth: String = ""
)