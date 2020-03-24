package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

data class UserData (

        @SerializedName("ID")
        val ID: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("birth")
        val birth: String = "",

        @SerializedName("address")
        val address: String = "",

        @SerializedName("phone")
        val phone: String = ""
)