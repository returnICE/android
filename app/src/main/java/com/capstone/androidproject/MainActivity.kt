package com.capstone.androidproject

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.capstone.androidproject.AddressSetting.GpsTracker
import com.capstone.androidproject.MainFragment.*
import com.capstone.androidproject.Response.SellerData
import com.capstone.androidproject.Response.SellerDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import com.capstone.androidproject.Response.SubedItemData
import com.capstone.androidproject.Response.SubedItmeDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.os.HandlerCompat.postDelayed
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler


class MainActivity : AppCompatActivity() {

    private val frag1: HomeFragment =
        HomeFragment()
    private val frag2: SearchFragment =
        SearchFragment()
    private val frag3: AlertFragment =
        AlertFragment()
    private val frag4: MypageFragment =
        MypageFragment()

    var sellers: ArrayList<SellerData> = ArrayList()
    var subeds: ArrayList<SubedItemData> ?= ArrayList()
    var page = 0

    var _mylocate=Location("alive")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.getResult()!!.getToken()
            })
        // https://blog.naver.com/ndb796/221553341369
        // https://blog.work6.kr/332
        // 푸시알람

//        val token =  FirebaseInstanceId.getInstance().getInstanceId()
//        Log.d("test1",token.toString()) // firebase 토큰 확인

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        ab!!.setDisplayShowTitleEnabled(false)

        bottomNavi.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.getItemId()) {
                    R.id.action_home -> setFrag(0)
                    R.id.action_search -> setFrag(1)
                    R.id.action_alert -> setFrag(2)
                    R.id.action_mypage -> setFrag(3)
                }
                return true
            }
        })

        var mylocate=Location("mylocate")
        val lat = App.prefs.lat
        val lon = App.prefs.lon
        if(lon != 0.0f && lat != 0.0f){
            mylocate.longitude = lon.toDouble()
            mylocate.latitude = lat.toDouble()
        }
        else {
            mylocate = getMyLocation()
        }
        _mylocate=mylocate

        getSubedItem()

        getSeller(mylocate, page)

    }

    private fun setFrag(n: Int) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        when (n) {
            0 -> {
                val bundle = Bundle()
                bundle.putSerializable("subeds", subeds)
                frag1.arguments = bundle
                ft.replace(R.id.Main_Frame, frag1)
                ft.commit()

            }
            1 -> {
                val bundle = Bundle()
                bundle.putSerializable("sellers", sellers)

                frag2.arguments = bundle

                ft.replace(R.id.Main_Frame, frag2)
                ft.commit()
            }
            2 -> {
                ft.replace(R.id.Main_Frame, frag3)
                ft.commit()
            }
            3 -> {
                val bundle = Bundle()
                bundle.putSerializable("subeds", subeds)

                frag4.arguments = bundle
                ft.replace(R.id.Main_Frame, frag4)
                ft.commit()
            }
        }
    }

    fun getMyLocation():Location {
        val gpsTracker: GpsTracker
        gpsTracker = GpsTracker(this)

        val mylat = gpsTracker.getLatitude()
        val mylon = gpsTracker.getLongitude()

        val mylocate = Location("myLoc")
        mylocate.latitude = mylat
        mylocate.longitude = mylon

        return mylocate
    }
    //https://webnautes.tistory.com/1315

    fun getSeller(mylocate: Location, page: Int) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        Log.d("locationtest",mylocate.latitude.toString())
        Log.d("locationtest",mylocate.longitude.toString())
        server.postSellerRequest(mylocate.latitude, mylocate.longitude, page,-1f).enqueue(object : Callback<SellerDataResponse> {
            override fun onFailure(call: Call<SellerDataResponse>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SellerDataResponse>, response: Response<SellerDataResponse>) {
                val success = response.body()!!.success
                val sellerdata = response.body()!!.sellerdata

                if (!success) {
                    Toast.makeText(this@MainActivity, "목록가져오기 실패", Toast.LENGTH_SHORT).show()
                } else {
                    for (seller in sellerdata) {
                        val sellerlocate = Location("myLoc")
                        sellerlocate.longitude = seller.lon
                        sellerlocate.latitude = seller.lat
                        val distance = mylocate.distanceTo(sellerlocate).toDouble()

                        sellers.add(
                            SellerData(
                                seller.sellerId,
                                seller.name,
                                "",
                                "",
                                seller.totalSubs,
                                seller.lat,
                                seller.lon,
                                distance,
                                seller.imgURL,
                                "hihi",
                                seller.type,
                                seller.minPrice
                            )
                        )
                    }
                    sellers.sortWith(compareBy({ it.distance }))
                }
            }
        })
    }

    fun getSubedItem(){
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getSubedItemRequest(App.prefs.token).enqueue(object:
            Callback<SubedItmeDataResponse> {
            override fun onFailure(call: Call<SubedItmeDataResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<SubedItmeDataResponse>,
                response: Response<SubedItmeDataResponse>
            ) {
                val success = response.body()!!.success
                val subdata = response.body()!!.subdata

                if(!success){
                    Toast.makeText(this@MainActivity, "목록가져오기 실패", Toast.LENGTH_SHORT).show()
                }
                else{
                    subeds?.clear()
                    for (subed in subdata) {
                        if(subedCheck(subed))
                            continue
                        subeds?.add(
                            SubedItemData(
                                "",
                                subed.subedId,
                                subed.startDate,
                                subed.endDate,
                                subed.term,
                                subed.limitTimes,
                                subed.autoPay,
                                subed.usedTimes,
                                subed.subId,
                                subed.subName,
                                subed.sellerId,
                                subed.name,
                                ""
                            )
                        )
                    }
                    setFrag(0) // 첫 프래그먼트 화면 지정
                }
            }
        })
    }

    fun subedCheck(sub:SubedItemData):Boolean{
        for(s in subeds!!.iterator()){
            if(s.name == sub.name)
                return true
        }
        return false
    }
}