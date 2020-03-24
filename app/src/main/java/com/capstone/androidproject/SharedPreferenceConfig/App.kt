package com.capstone.androidproject.SharedPreferenceConfig

import android.app.Application

class App : Application(){
    companion object{
        lateinit var prefs : UserPreference
    }

    override fun onCreate() {
        prefs =UserPreference(applicationContext)
        super.onCreate()
    }
}