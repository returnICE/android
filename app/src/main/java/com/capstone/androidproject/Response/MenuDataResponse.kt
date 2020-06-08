package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class MenuDataResponse {
    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("menuItem")
    val menuItem:List<MenuData> = ArrayList()
}