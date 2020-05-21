package com.capstone.androidproject.MainFragment.Mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.androidproject.MainActivity
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.Success
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_modification_info.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModificationInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modification_info)


        MIbtnConfirmModification.setOnClickListener{
            var pw = MItextPassword.text.toString()
            var pwConfirm = MItextPasswordConfirm.text.toString()
            if (pw != pwConfirm){
                Toast.makeText(this@ModificationInfoActivity, "비밀번호를 다시 확인해 주세요", Toast.LENGTH_SHORT).show()
            }
            else {
                Log.d("testing", "pw in ModificationInfoActivity : "+pw)
                confirm(App.prefs.token, pw)
            }
        }
    }
    fun confirm(token: String, pw: String) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.putCustomerInfoRequest(token, pw).enqueue(object : Callback<Success> {
            override fun onFailure(call: Call<Success>?, t: Throwable?) {
                Toast.makeText(this@ModificationInfoActivity, "변경 실패1", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Success>?, response: Response<Success>) {
                val success = response.body()?.success

                if (success == false) {
                    Toast.makeText(this@ModificationInfoActivity, "변경 실패2", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ModificationInfoActivity, "비밀번호 변경 성공", Toast.LENGTH_SHORT).show()
                    val nextIntent = Intent(this@ModificationInfoActivity, MainActivity::class.java)
                    startActivity(nextIntent)

                }
            }
        })
    }
}