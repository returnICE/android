package com.capstone.androidproject

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.androidproject.Response.LoginResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (App.prefs.token != "") {
            login(App.prefs.token)
            SystemClock.sleep(300)
        }
        //로그인 돼있으면 바로 로그인
        //shared preference에 정보 저장돼있으면 로그인 돼있는걸로 판단함

        else{
            startActivity<LoginActivity>()
            finish()
        }
    }
    fun login(token: String) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getGetUserRequest(token).enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                Toast.makeText(this@SplashActivity, "로그인 실패1", Toast.LENGTH_SHORT).show()
                startActivity<LoginActivity>()
                finish()
            }

            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
                val success = response?.body()?.success

                if (success == false) {
                    Toast.makeText(this@SplashActivity, "로그인 실패2", Toast.LENGTH_SHORT).show()
                    startActivity<LoginActivity>()
                    finish()
                } else {
                    Toast.makeText(this@SplashActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    startActivity<MainActivity>()
                    finish()
                }
            }
        })
    }

//    fun login(id: String, pw: String) {
//        val serverConnect = ServerConnect(this)
//        val server = serverConnect.conn()
//
//        server.postLoginRequest(id, pw).enqueue(object : Callback<LoginResponse> {
//            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
//                Toast.makeText(this@SplashActivity, "로그인 실패1", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>?) {
//                val success = response?.body()?.success
//
//                if (success == false) {
//                    Toast.makeText(this@SplashActivity, "로그인 실패2", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this@SplashActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
//    }
}
