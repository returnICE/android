package com.capstone.androidproject.SharedPreferenceConfig

import android.content.Context
import android.content.SharedPreferences

class UserPreference (context: Context){
    val PREFS_FILENAME = "user"
    val PREF_KEY_ID="ID"
    val PREF_KEY_PW="PW"
    val PREF_KEY_NAME="name"
    val PREF_KEY_BIRTH="birth"
    val PREF_KEY_ADDRESS="address"
    val PREF_KEY_PHONE="PHONE"

    val PREF_KEY_TOKEN="token"

    var prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME,0)

    var name: String
        get() = prefs.getString(PREF_KEY_NAME,"")
        set(value) = prefs.edit().putString(PREF_KEY_NAME,value).apply()
    var address: String
        get() = prefs.getString(PREF_KEY_ADDRESS,"")
        set(value) = prefs.edit().putString(PREF_KEY_ADDRESS,value).apply()
    var token: String
        get() = prefs.getString(PREF_KEY_TOKEN,"")
        set(value) = prefs.edit().putString(PREF_KEY_TOKEN,value).apply()

    fun clear(){
        prefs.edit().clear().apply()
    }
}

// https://blog.yena.io/studynote/2017/12/18/Android-Kotlin-SharedPreferences.html
// https://shacoding.com/2019/08/15/android-db%ec%97%86%ec%9d%b4-%ec%99%b8%eb%b6%80-%eb%8d%b0%ec%9d%b4%ed%84%b0-%eb%b6%88%eb%9f%ac%ec%98%a4%ea%b8%b0-with-%ec%bd%94%ed%8b%80%eb%a6%b0/
// shared preference에 대해 공부할 것
