package com.capstone.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.capstone.androidproject.Response.Success
import com.capstone.androidproject.ServerConfig.ServerConnect
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.activity_signup.textId
import kotlinx.android.synthetic.main.activity_signup.textPassword

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btnNext.setOnClickListener{

            var id = textId.text.toString()
            var pw = textPassword.text.toString()
            val nextIntent = Intent(this@SignupActivity, SignupDetailActivity::class.java)

            //다음 activity로 id, pw 전송
            nextIntent.putExtra("id", id)
            nextIntent.putExtra("pw", pw)
            startActivity(nextIntent)
        }
        btnCheckId.setOnClickListener(){
            var id = textId.text.toString()
            checkId(id)
        }
    }


    fun checkId(id: String){
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()
        if(id == ""){
            Toast.makeText(this@SignupActivity, "id를 입력해 주세요", Toast.LENGTH_SHORT).show()
        }
        else {
            server.getCheckRequest(id).enqueue(object
                : Callback<Success> {
                override fun onFailure(call: Call<Success>, t: Throwable) {
                    Toast.makeText(this@SignupActivity, "중복체크 실패1", Toast.LENGTH_SHORT).show()
                    println(t?.message.toString())
                }


                override fun onResponse(call: Call<Success>, response: Response<Success>) {
                    val succ = response?.body()

                    if (succ?.success == false) {
                        Toast.makeText(this@SignupActivity, "중복입니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@SignupActivity, "사용 가능한 ID입니다", Toast.LENGTH_SHORT)
                            .show()
                    }

                }

            })
        }
    }


}
