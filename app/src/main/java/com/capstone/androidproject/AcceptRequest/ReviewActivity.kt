package com.capstone.androidproject.AcceptRequest

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
import kotlinx.android.synthetic.main.activity_review.*
import org.jetbrains.anko.radioGroup
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val menuName = intent.getStringExtra("menuName")
        val serviceName = intent.getStringExtra("serviceName")
        val sellerName = intent.getStringExtra("sellerName")
        val currentTime = intent.getStringExtra("currentTime")
        val eatenId = intent.getIntExtra("eatenId", 0)

        Log.d("testing", "eatenId in ReviewActivity" + eatenId.toString())
        Log.d("testing", "menuName in ReviewActivity" + menuName)
        RVtextDate.setText(currentTime)
        RVtextMenuName.setText(menuName)
        RVtextServiceName.setText(serviceName)
        RVtextStoreName.setText(sellerName)

        var score = 1;

        radioGroup1.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.radio1 -> {
                    score = 1
                    Toast.makeText(this, "1점", Toast.LENGTH_SHORT).show()
                }
                R.id.radio2 -> {
                    score = 2
                    Toast.makeText(this, "2점", Toast.LENGTH_SHORT).show()
                }
                R.id.radio3 -> {
                    score = 3
                    Toast.makeText(this, "3점", Toast.LENGTH_SHORT).show()
                }
                R.id.radio4 -> {
                    score = 4
                    Toast.makeText(this, "4점", Toast.LENGTH_SHORT).show()
                }
                R.id.radio5 -> {
                    score = 5
                    Toast.makeText(this, "5점", Toast.LENGTH_SHORT).show()
                }
            }
        }
        RVbtnCertification.setOnClickListener() {
            review(App.prefs.token, eatenId, score)
        }


    }

    fun review(token: String, eatenId: Int, score: Int) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.postReviewRequest(token, eatenId, score).enqueue(object :
            Callback<Success> {
            override fun onFailure(call: Call<Success>, t: Throwable) {
                Toast.makeText(this@ReviewActivity, "승인 실패1", Toast.LENGTH_SHORT).show()
                println(t?.message.toString())
            }

            override fun onResponse(
                call: Call<Success>,
                response: Response<Success>
            ) {
                val success = response?.body()?.success

                if (success == false) {
                    Toast.makeText(this@ReviewActivity, "승인 실패2", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ReviewActivity, "승인 성공", Toast.LENGTH_SHORT).show()
                    val nextIntent = Intent(this@ReviewActivity, MainActivity::class.java)
                    startActivity(nextIntent)
                }
            }
        })
    }

}