package com.capstone.androidproject.Response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CampaignData (

    @SerializedName("customerId")
    val customerId: String = "",

    @SerializedName("campaignId")
    val campaignId: Int = 0,

    @SerializedName("ccId")
    val ccId: Int = 0,

    @SerializedName("Campaign")
    var campaign:Campaign = Campaign()
): Parcelable