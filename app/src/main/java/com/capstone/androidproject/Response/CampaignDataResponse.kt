package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class CampaignDataResponse {
    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("campaign")
    val campaign:List<CampaignData> = ArrayList()
}