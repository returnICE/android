package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName
data class EnterpriseData (
    @SerializedName("enterpriseId")
    val enterpriseId: String = "",

    @SerializedName("approval")
    val approval: Int = 0
)