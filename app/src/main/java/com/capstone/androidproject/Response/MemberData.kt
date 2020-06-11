package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName
data class MemberData (
    @SerializedName("enterpriseId")
    val enterpriseId: String = "",

    @SerializedName("approval")
    val approval: Int = 0,

    @SerializedName("amountPerDay")
    val amountPerDay: Int = 0,

    @SerializedName("amountPerMonth")
    val amountPerMonth: Int = 0,

    @SerializedName("resetDate")
    val resetDate: String = ""


)