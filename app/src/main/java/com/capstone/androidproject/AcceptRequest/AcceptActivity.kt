package com.capstone.androidproject.AcceptRequest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.capstone.androidproject.R
import kotlinx.android.synthetic.main.activity_accept.*
import java.time.LocalDateTime

class AcceptActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept)

        val price = intent.getStringExtra("price")
        val menuName = intent.getStringExtra("menuName")
        val serviceName = intent.getStringExtra("serviceName")
        val sellerName = intent.getStringExtra("sellerName")
        val currentTime: LocalDateTime = LocalDateTime.now()

        ACtextMenuName.setText(menuName)
        ACtextPrice.setText(price)
        ACtextServiceName.setText(serviceName)
        ACtextStoreName.setText(sellerName)
        ACtextDate.setText(currentTime.toString())

        ACbtnCertification.setOnClickListener(){

        }



    }

}