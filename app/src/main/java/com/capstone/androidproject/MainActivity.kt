package com.capstone.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import com.capstone.androidproject.Fragment.*
import com.capstone.androidproject.SharedPreferenceConfig.App
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val frag1: HomeFragment =
        HomeFragment()
    private val frag2: CategoryFragment =
        CategoryFragment()
    private val frag3: SearchFragment =
        SearchFragment()
    private val frag4: AlertFragment =
        AlertFragment()
    private val frag5: MypageFragment =
        MypageFragment()

    lateinit var myAddress:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener {task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.getResult()!!.getToken()
                Log.d("test1",token)
            })
        // https://blog.naver.com/ndb796/221553341369
        // https://blog.work6.kr/332
        // 푸시알람

//        val token =  FirebaseInstanceId.getInstance().getInstanceId()
//        Log.d("test1",token.toString()) // firebase 토큰 확인

        if(intent.hasExtra("myAddress")){
            myAddress = intent.getStringExtra("myAddress")
        }
        else{
            myAddress = App.prefs.address
        }

        val toolbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayShowTitleEnabled(false)

        bottomNavi.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.getItemId()) {
                    R.id.action_home -> setFrag(0)
                    R.id.action_category -> setFrag(1)
                    R.id.action_search -> setFrag(2)
                    R.id.action_alert -> setFrag(3)
                    R.id.action_mypage -> setFrag(4)
                }
                return true
            }
        })

        setFrag(0) // 첫 프래그먼트 화면 지정
    }

    private fun setFrag(n: Int) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        when (n) {
            0 -> {
                val bundle = Bundle()
                bundle.putString("myAddress",myAddress)

                frag1.arguments = bundle

                ft.replace(R.id.Main_Frame, frag1)
                ft.commit()
            }

            1 -> {
                ft.replace(R.id.Main_Frame, frag2)
                ft.commit()
            }

            2 -> {
                ft.replace(R.id.Main_Frame, frag3)
                ft.commit()
            }
            3 -> {
                ft.replace(R.id.Main_Frame, frag4)
                ft.commit()
            }
            4 -> {
                ft.replace(R.id.Main_Frame, frag5)
                ft.commit()
            }
        }
    }
}