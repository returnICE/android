package com.capstone.androidproject.AddressSetting

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.capstone.androidproject.R
import kotlinx.android.synthetic.main.activity_my_address_setting.*
import org.jetbrains.anko.startActivity

class MyAddressSettingActivity : AppCompatActivity() {


    private val SEARCH_ADDRESS_ACTIVITY = 10000

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

        btnFindTextAddress.setOnClickListener {
            val intent = Intent(this@MyAddressSettingActivity, WebViewFindTextAddressActivity::class.java)
                                    startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
        }

        btnSearchAddress.setOnClickListener {

            val intent = Intent(this@MyAddressSettingActivity, DetailAddressActivity::class.java)
            intent.putExtra("lat",lat)
            intent.putExtra("lon",lon)
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
        }

        btnAddressDecide.setOnClickListener {

            val extra = Bundle()
            val intent = Intent()

            val address = textAddress.text.toString()

            extra.putString("address", address)
            intent.putExtras(extra)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onActivityResult( requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY ->
                if (resultCode == RESULT_OK) {
                    val data = intent?.extras!!.getString("data")
                    if (data != null)
                        textAddress.setText(data)

                }
        }
    }
}
