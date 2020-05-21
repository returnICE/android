package com.capstone.androidproject.SharedPreferenceConfig

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.capstone.androidproject.R

class App : Application(){

    companion object{
        lateinit var prefs : UserPreference
        lateinit var instance: App
    }

    override fun onCreate() {
        prefs =UserPreference(applicationContext)
        instance = this
        super.onCreate()
    }

    internal var progressDialog: AppCompatDialog? = null

    fun progressON(activity: Activity?, message: String?) {

        if (activity == null || activity.isFinishing) {
            return
        }


        if (progressDialog != null && progressDialog!!.isShowing) {
            progressSET(message!!)
        } else {

            progressDialog = AppCompatDialog(activity)
            progressDialog!!.setCancelable(false)
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog!!.setContentView(R.layout.progress_loading)
            progressDialog!!.show()

        }


        val img_loading_frame = progressDialog!!.findViewById(R.id.iv_frame_loading) as ImageView?
        val frameAnimation = img_loading_frame!!.getBackground() as AnimationDrawable
        img_loading_frame!!.post(Runnable { frameAnimation.start() })

        val tv_progress_message =
            progressDialog!!.findViewById(R.id.tv_progress_message) as TextView?
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message!!.text = message
        }


    }

    fun progressSET(message: String) {

        if (progressDialog == null || !progressDialog!!.isShowing) {
            return
        }


        val tv_progress_message =
            progressDialog!!.findViewById(R.id.tv_progress_message) as TextView?
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message!!.text = message
        }

    }

    fun progressOFF() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }
}