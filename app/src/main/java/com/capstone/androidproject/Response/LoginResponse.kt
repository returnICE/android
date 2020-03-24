package com.capstone.androidproject.Response

import com.google.gson.annotations.SerializedName

class LoginResponse {

    @SerializedName("success")
    val success:Boolean = false

    @SerializedName("user")
    val user = UserData()


}
// response 받는 데이터 형식에 따라 class, data class 맞춰서 만들어야함
// https://jongmin92.github.io/2018/01/29/Programming/android-retrofit2-okhttp3/