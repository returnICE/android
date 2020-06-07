package com.capstone.androidproject.MainFragment.Mypage

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
import kotlinx.android.synthetic.main.activity_enterprise_code.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnterpriseCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enterprise_code)

        ECbtnRegister.setOnClickListener {

            registerCode(App.prefs.token, ECtextName.text.toString())
        }
    }


    fun registerCode(token: String, enterpriseCode: String) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()
        if (enterpriseCode == "") {
            Toast.makeText(this@EnterpriseCodeActivity, "코드를 입력해 주세요", Toast.LENGTH_SHORT).show()
        } else {
            server.postEnterpriseCodeRequest(token, enterpriseCode).enqueue(object
                : Callback<Success> {
                override fun onFailure(call: Call<Success>, t: Throwable) {
                    Toast.makeText(this@EnterpriseCodeActivity, "코드 등록 실패1", Toast.LENGTH_SHORT).show()
                    println(t?.message.toString())
                }
                override fun onResponse(call: Call<Success>, response: Response<Success>) {
                    val succ = response?.body()

                    if (succ?.success == false) {
                        Toast.makeText(this@EnterpriseCodeActivity, "코드 등록 실패2", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@EnterpriseCodeActivity, "코드 등록 성공", Toast.LENGTH_SHORT).show()
                        Log.d("testing", "enterpriseCode : " + enterpriseCode)
                        val nextIntent = Intent(this@EnterpriseCodeActivity, MainActivity::class.java)
                        startActivity(nextIntent)
                    }
                }
            })
        }
    }


}