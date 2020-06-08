package com.capstone.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_campaign.*
import org.jetbrains.anko.startActivity

class CampaignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign)

        val title = intent.getStringExtra("pushTitle")
        val body = intent.getStringExtra("pushBody")
        pushTitle.text = title
        pushBody.text = body

        btnPushCheck.setOnClickListener {
            startActivity<MainActivity>()
            finishAffinity()
        }
    }
}
