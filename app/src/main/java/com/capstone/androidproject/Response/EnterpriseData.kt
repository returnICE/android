package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName
data class EnterpriseData (
    @SerializedName("enterpriseId")
    val enterpriseId: String = "",

    @SerializedName("name")
    val name: String = "",

    @SerializedName("amountPerDay")
    val amountPerDay: Int = 0,

    @SerializedName("amountPerMonth")
    val amountPerMonth: Int = 0


)