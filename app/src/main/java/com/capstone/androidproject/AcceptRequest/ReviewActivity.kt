package com.capstone.androidproject.AcceptRequest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.androidproject.R
import kotlinx.android.synthetic.main.activity_review.*
import org.jetbrains.anko.radioGroup
import org.jetbrains.anko.toast

class ReviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        val menuName = intent.getStringExtra("menuName")
        val serviceName = intent.getStringExtra("serviceName")
        val sellerName = intent.getStringExtra("sellerName")
        val currentTime = intent.getStringExtra("currentTime")


        RVtextDate.setText(currentTime)
        RVtextMenuName.setText(menuName)
        RVtextServiceName.setText(serviceName)
        RVtextStoreName.setText(sellerName)

        radioGroup1.setOnCheckedChangeListener{radioGroup, i ->
            when(i){
                R.id.radio1 ->
                    Toast.makeText(this, "1점", Toast.LENGTH_SHORT).show()
                R.id.radio2 ->
                    Toast.makeText(this, "2점", Toast.LENGTH_SHORT).show()
                R.id.radio3 ->
                    Toast.makeText(this, "3점", Toast.LENGTH_SHORT).show()
                R.id.radio4 ->
                    Toast.makeText(this, "4점", Toast.LENGTH_SHORT).show()
                R.id.radio5 ->
                    Toast.makeText(this, "5점", Toast.LENGTH_SHORT).show()
            }
        }

    }
}