package com.capstone.androidproject.SharedPreferenceConfig

import android.content.Context
import android.content.SharedPreferences

class UserPreference (context: Context){
    val PREFS_FILENAME = "user"
    val PREF_KEY_ID="ID"
    val PREF_KEY_NAME="name"
    val PREF_KEY_ADDRESS="address"
    val PREF_KEY_LATITUDE="latitude"
    val PREF_KEY_LONGITUDE="longitude"
    val PREF_KEY_ENTERPRISEID="enterpriseId"
    val PREF_KEY_ENTERPRISEAPPROVAL="enterpriseApproval"
    val PREF_KEY_AMOUNTPERDAY="AmountPerDay"
    val PREF_KEY_AMOUNTPERMONTH="AmountPerMonth"
    val PREF_KEY_USEDAMOUNTPERDAY="usedAmountPerDay"
    val PREF_KEY_USEDAMOUNTPERMONTH="usedAmountPerMonth"
    val PREF_KEY_RESETDATE="resetDate"

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
    var id: String
        get() = prefs.getString(PREF_KEY_ID,"")
        set(value) = prefs.edit().putString(PREF_KEY_ID,value).apply()
    var lat: Float
        get() = prefs.getFloat(PREF_KEY_LATITUDE,0.0f)
        set(value) = prefs.edit().putFloat(PREF_KEY_LATITUDE,value).apply()
    var lon: Float
        get() = prefs.getFloat(PREF_KEY_LONGITUDE,0.0f)
        set(value) = prefs.edit().putFloat(PREF_KEY_LONGITUDE,value).apply()
    var enterpriseId: String
        get() = prefs.getString(PREF_KEY_ENTERPRISEID,"")
        set(value) = prefs.edit().putString(PREF_KEY_ENTERPRISEID,value).apply()
    var enterpriseApproval: Int
        get() = prefs.getInt(PREF_KEY_ENTERPRISEAPPROVAL,0)
        set(value) = prefs.edit().putInt(PREF_KEY_ENTERPRISEAPPROVAL,value).apply()
    var usedAmountPerDay: Int
        get() = prefs.getInt(PREF_KEY_USEDAMOUNTPERDAY,0)
        set(value) = prefs.edit().putInt(PREF_KEY_USEDAMOUNTPERDAY,value).apply()
    var usedAmountPerMonth: Int
        get() = prefs.getInt(PREF_KEY_USEDAMOUNTPERMONTH,0)
        set(value) = prefs.edit().putInt(PREF_KEY_USEDAMOUNTPERMONTH,value).apply()
    var amountPerDay: Int
        get() = prefs.getInt(PREF_KEY_AMOUNTPERDAY,0)
        set(value) = prefs.edit().putInt(PREF_KEY_AMOUNTPERDAY,value).apply()
    var amountPerMonth: Int
        get() = prefs.getInt(PREF_KEY_AMOUNTPERMONTH,0)
        set(value) = prefs.edit().putInt(PREF_KEY_AMOUNTPERMONTH,value).apply()
    var resetDate: String
        get() = prefs.getString(PREF_KEY_RESETDATE,"")
        set(value) = prefs.edit().putString(PREF_KEY_RESETDATE,value).apply()

    fun clear(){
        prefs.edit().clear().apply()
    }
}

// https://blog.yena.io/studynote/2017/12/18/Android-Kotlin-SharedPreferences.html
// https://shacoding.com/2019/08/15/android-db%ec%97%86%ec%9d%b4-%ec%99%b8%eb%b6%80-%eb%8d%b0%ec%9d%b4%ed%84%b0-%eb%b6%88%eb%9f%ac%ec%98%a4%ea%b8%b0-with-%ec%bd%94%ed%8b%80%eb%a6%b0/
// shared preference에 대해 공부할 것
