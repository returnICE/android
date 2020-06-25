package com.capstone.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.capstone.androidproject.Response.*
import com.capstone.androidproject.ServerConfig.HttpService
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignup.setOnClickListener{
            val signupIntent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(signupIntent)
        }
        btnLogin.setOnClickListener {
            val customerId = textId.text.toString()
            val pw = textPassword.text.toString()
            login(customerId, pw)
        }
    }

    fun login(customerId: String, pw: String) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.postLoginRequest(customerId, pw).enqueue(object : Callback<TokenResponse> {
            override fun onFailure(call: Call<TokenResponse>?, t: Throwable?) {
                //Toast.makeText(this@LoginActivity, "로그인 실패1", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<TokenResponse>?, response: Response<TokenResponse>) {
                val success = response.body()?.success
                val token = response.body()!!.data

                if (success == false) {
                    //Toast.makeText(this@LoginActivity, "로그인 실패2", Toast.LENGTH_SHORT).show()
                } else {
                    App.prefs.token = token.toString()// 로그인 성공하면 shared_Preference에 유저정보 저장
                    getEnterprise(token)
                    getUserInfo(server, token)
                }
            }
        })
    }

    fun getEnterprise(token: String) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getMemberDataRequest(token).enqueue(object:

            Callback<MemberDataResponse> {
            override fun onFailure(call: Call<MemberDataResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "받아오기 실패1", Toast.LENGTH_SHORT).show()
                Log.d("testing","err msg : " + t?.message.toString())
            }
            override fun onResponse(
                call: Call<MemberDataResponse>,
                response: Response<MemberDataResponse>
            ) {
                val success = response?.body()?.success
                val enterdata = response?.body()?.memberdata
                if (success == false) {
                    Toast.makeText(this@LoginActivity, "받아오기 실패2", Toast.LENGTH_SHORT).show()
                } else {
                    if(enterdata?.approval == 1){
                        App.prefs.enterpriseId = enterdata?.enterpriseId
                        App.prefs.enterpriseApproval = enterdata?.approval
                    }
                }
            }
        })
    }

    fun getUserInfo(server:HttpService, token: String){
        server.getCustomerInfoRequest(token).enqueue(object : Callback<UserInfoResponse>{
            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "사용자 이름 받아오기 실패", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                val succ = response.body()?.success
                val userinfo = response.body()?.data

                if (succ == false) {
                    Toast.makeText(this@LoginActivity, "사용자 이름 받아오기 실패 2", Toast.LENGTH_SHORT).show()
                } else {
                    App.prefs.name = userinfo?.name.toString()
                    App.prefs.id = userinfo?.customerId.toString()
                    Log.d("testing","name : " + userinfo?.name.toString())

                    startActivity<MainActivity>()
                    finish()
                }
            }
        })
    }
}


// 토큰인증방식은 아직 안함.
// https://kimch3617.tistory.com/entry/Retrofit%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%ED%86%A0%ED%81%B0-%EC%9D%B8%EC%A6%9D-%EB%B0%A9%EC%8B%9D-%EA%B5%AC%ED%98%84

// 이건 쿠키/세션 유지
// https://gun0912.tistory.com/50

// 이 사이트 앞으로도 도움 좀 될듯
// http://blog.yena.io/studynote/
