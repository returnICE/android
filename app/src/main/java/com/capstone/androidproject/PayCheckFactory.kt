package com.capstone.androidproject

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.webkit.JavascriptInterface

class PayCheckFactory(activity: Activity) {

    private val activity = activity

    @JavascriptInterface
    fun resultPay(message: String) {
        val intent = Intent()

        if (message == "success") {
            intent.putExtra("result", "success")
            activity.setResult(RESULT_OK, intent)
            activity.finish()
        } else {
            intent.putExtra("result", "failure")
            activity.setResult(RESULT_OK, intent)
            activity.finish()
        }
    }
}