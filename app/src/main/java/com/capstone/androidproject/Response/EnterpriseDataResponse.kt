package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class EnterpriseDataResponse {

    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("enterprisedata")
    val enterprisedata:EnterpriseData = EnterpriseData()

}