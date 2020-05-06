package com.capstone.androidproject.AddressSetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.capstone.androidproject.R
import kotlinx.android.synthetic.main.activity_my_address_setting.*
import org.jetbrains.anko.startActivity

class MyAddressSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_address_setting)

        btnBack.setOnClickListener {
            finish()
            overridePendingTransition(
                R.anim.slide_stay,
                R.anim.slide_down
            )
        }

        val lat = intent.getDoubleExtra("lat",37.279)
        val lon = intent.getDoubleExtra("lon",127.043)
        Log.d("maplocation_MyAddressSetting",lat.toString())
        Log.d("maplocation_MyAddressSetting",lon.toString())
        btnSearchAddress.setOnClickListener {
            startActivity<DetailAddressActivity>(
                "lat" to lat,
                "lon" to lon)
        }
    }
}
