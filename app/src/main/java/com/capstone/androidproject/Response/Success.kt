package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

data class Success (

    @SerializedName("success")
    val success: Boolean = false

)