package com.capstone.androidproject

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.capstone.androidproject.AddressSetting.GpsTracker
import com.capstone.androidproject.MainFragment.*
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.capstone.androidproject.Response.*


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
    var subeds: ArrayList<SubedItemData>? = ArrayList()
    var b2bs: ArrayList<B2BData>? = ArrayList()
    var alerts:ArrayList<CampaignData> ?= ArrayList()

    var page = 0

    var _mylocate = Location("alive")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.getResult()!!.getToken()
                Log.d("test1_fcmtoken",token) // firebase 토큰 확인
                sendFCMToken(token)
                // Get new Instance ID token
            })
        // https://blog.naver.com/ndb796/221553341369
        // https://blog.work6.kr/332
        // 푸시알람
        Log.d("test1_logintoken",App.prefs.token) // firebase 토큰 확인

        if(intent.hasExtra("pushSellerName")){
            setFrag(2)
        }

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

        var mylocate = Location("mylocate")
        val lat = App.prefs.lat
        val lon = App.prefs.lon
        if (lon != 0.0f && lat != 0.0f) {
            mylocate.longitude = lon.toDouble()
            mylocate.latitude = lat.toDouble()
        } else {
            mylocate = getMyLocation()
        }
        _mylocate = mylocate

        getb2bdata()
        getSubedItem()
        getCampaignItem()

        getMember(App.prefs.token)
        getEnterprise(App.prefs.enterpriseId)
        getSeller(mylocate, page)

    }

    private fun setFrag(n: Int) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        when (n) {
            0 -> {
                val bundle = Bundle()
                bundle.putSerializable("subeds", subeds)
                bundle.putSerializable("b2bs", b2bs)
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
                val bundle = Bundle()
                bundle.putSerializable("alerts", alerts)
                frag3.arguments = bundle

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

    fun getMyLocation(): Location {
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

        server.postSellerRequest(mylocate.latitude, mylocate.longitude, page,-1f).enqueue(object : Callback<SellerDataResponse> {
            override fun onFailure(call: Call<SellerDataResponse>?, t: Throwable?) {
                Toast.makeText(this@MainActivity, "getSeller 통신 실패", Toast.LENGTH_SHORT).show()
            }
                override fun onResponse(
                    call: Call<SellerDataResponse>,
                    response: Response<SellerDataResponse>
                ) {
                    val success = response.body()!!.success
                    val sellerdata = response.body()!!.sellerdata

                    if (!success) {
                        Toast.makeText(this@MainActivity, "getSeller 목록가져오기 실패", Toast.LENGTH_SHORT).show()
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

    fun getSubedItem() {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getSubedItemRequest(App.prefs.token).enqueue(object :
            Callback<SubedItmeDataResponse> {
            override fun onFailure(call: Call<SubedItmeDataResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "getSubedItem 통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<SubedItmeDataResponse>,
                response: Response<SubedItmeDataResponse>
            ) {
                val success = response.body()!!.success
                val subdata = response.body()!!.subdata

                if (!success) {
                    //Toast.makeText(this@MainActivity, "getSubedItem 목록가져오기 실패", Toast.LENGTH_SHORT).show()
                } else {
                    subeds?.clear()
                    for (subed in subdata) {
                        if (subedCheck(subed))
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
                                subed.imgURL
                            )
                        )
                    }
                    setFrag(0) // 첫 프래그먼트 화면 지정
                }
            }
        })
    }

    fun sendFCMToken(fcmtoken:String){
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        val token=App.prefs.token
        server.postRegisterFCMTokenRequest(token,fcmtoken).enqueue(object: Callback<Success> {
            override fun onFailure(call: Call<Success>, t: Throwable) {
                Toast.makeText(this@MainActivity, "FCM Token 통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Success>,response: Response<Success>) {
                val success = response.body()!!.success

                if(!success){
                    Toast.makeText(this@MainActivity, "FCM Token 등록 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun getCampaignItem(){
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getCampaignItemRequest(App.prefs.token).enqueue(object:
            Callback<CampaignDataResponse> {
            override fun onFailure(call: Call<CampaignDataResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "campaign 통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CampaignDataResponse>,
                response: Response<CampaignDataResponse>) {
                val success = response.body()!!.success
                val campaignlist = response.body()!!.campaign

                if(!success){
                    //Toast.makeText(this@MainActivity, "campaign 목록가져오기 실패", Toast.LENGTH_SHORT).show()
                }
                else{
                    alerts?.clear()
                    for (alert in campaignlist) {
                        alerts?.add(
                            CampaignData(
                                alert.customerId,
                                alert.campaignId,
                                alert.ccId,
                                alert.campaign
                            )
                        )
                    }
                    alerts?.sortWith(compareBy({it.campaign.transmitDate}))
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

    fun b2bCheck(b2b: B2BData): Boolean {
        for (s in b2bs!!.iterator()) {
            if (s.name == b2b.name)
                return true
        }
        return false
    }

    fun getMember(token: String) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getMemberDataRequest(token).enqueue(object:

            Callback<MemberDataResponse> {
            override fun onFailure(call: Call<MemberDataResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "member 받아오기 실패1", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(
                call: Call<MemberDataResponse>,
                response: Response<MemberDataResponse>
            ) {
                val success = response?.body()?.success
                val memberdata = response?.body()?.memberdata
                if (success == false) {
                    //Toast.makeText(this@MainActivity, "member 받아오기 실패2", Toast.LENGTH_SHORT).show()
                } else {
                    if(memberdata?.approval == 1){
                        App.prefs.enterpriseId = memberdata?.enterpriseId
                        App.prefs.enterpriseApproval = memberdata?.approval
                        App.prefs.usedAmountPerDay = memberdata?.amountPerDay
                        App.prefs.usedAmountPerMonth = memberdata?.amountPerMonth
                        App.prefs.resetDate = memberdata?.resetDate

                    }
                }
            }
        })
    }
    fun getEnterprise(enterpriseId : String) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getEnterpriseDataRequest(enterpriseId).enqueue(object:

            Callback<EnterpriseDataResponse> {
            override fun onFailure(call: Call<EnterpriseDataResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "enterprise 받아오기 실패1", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(
                call: Call<EnterpriseDataResponse>,
                response: Response<EnterpriseDataResponse>
            ) {
                val success = response?.body()?.success
                val enterdata = response?.body()?.enterprisedata
                if (success == false) {
                    //Toast.makeText(this@MainActivity, "enterprise 받아오기 실패2", Toast.LENGTH_SHORT).show()
                } else {
                    if(App.prefs.enterpriseApproval == 1){

                        App.prefs.amountPerDay = enterdata!!.amountPerDay
                        App.prefs.amountPerMonth = enterdata?.amountPerMonth
                    }
                }
            }
        })
    }


    fun getb2bdata() {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.postB2BdataRequest(App.prefs.token).enqueue(object :
            Callback<B2BDataResponse> {
            override fun onFailure(call: Call<B2BDataResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "getb2bdata 통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<B2BDataResponse>,
                response: Response<B2BDataResponse>
            ) {
                val success = response.body()!!.success
                val b2bdata = response.body()!!.b2bdata

                if (!success) {
                    //Toast.makeText(this@MainActivity, "getb2bdata 목록가져오기 실패", Toast.LENGTH_SHORT).show()
                } else {
                    b2bs?.clear()
                    for (b2b in b2bdata) {
                        if ((b2bCheck(b2b)))
                            continue
                        b2bs?.add(
                            B2BData(
                                b2b.sellerId,
                                b2b.name,
                                b2b.minPrice,
                                b2b.imgURL
                            )
                        )
                    }
                }
            }
        })
    }
}