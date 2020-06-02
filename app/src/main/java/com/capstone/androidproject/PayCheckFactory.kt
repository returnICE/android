package com.capstone.androidproject

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface

class PayCheckFactory(activity: Activity) {

    private val _activity = activity

    @JavascriptInterface
    fun resultPay(message: String) {
        val intent = Intent()

        if (message == "success") {
            intent.putExtra("result", "success")
            _activity.setResult(RESULT_OK, intent)
            _activity.finish()
        } else {
            intent.putExtra("result", "failure")
            _activity.setResult(RESULT_OK, intent)
            _activity.finish()
        }
    }
}