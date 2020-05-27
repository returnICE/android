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
        val eatenId = intent.getIntExtra("eatenId", 0)

        RVtextMenuName.setText(menuName)
        RVtextServiceName.setText(serviceName)
        RVtextStoreName.setText(sellerName)

        var score = 3

        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> run{
            score = rating.toInt()
        } }

        RVbtnCertification.setOnClickListener {
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
                    finish()
                }
            }
        })
    }

}