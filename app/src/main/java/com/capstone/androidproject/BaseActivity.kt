package com.capstone.androidproject

import androidx.appcompat.app.AppCompatActivity
import com.capstone.androidproject.SharedPreferenceConfig.App


class BaseActivity : AppCompatActivity() {


    fun progressON() {
        App.instance.progressON(this, null)
    }

    fun progressON(message: String) {
        App.instance.progressON(this, message)
    }

    fun progressOFF() {
        App.instance.progressOFF()
    }

}