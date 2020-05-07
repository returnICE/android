package com.capstone.androidproject.MainFragment

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.capstone.androidproject.AddressSetting.GpsTracker
import com.capstone.androidproject.AddressSetting.MyAddressSettingActivity
import com.capstone.androidproject.MainActivity
import com.capstone.androidproject.MainFragment.StoreList.EndlessRecyclerViewScrollListener
import com.capstone.androidproject.MainFragment.StoreList.FindToMapActivity
import com.capstone.androidproject.MainFragment.StoreList.LinearLayoutManagerWrapper
import com.capstone.androidproject.MainFragment.StoreList.StoreListRecyclerAdapter
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.SellerData
import com.capstone.androidproject.Response.SellerDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_address_setting.*
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {

    private var swipeContainer: SwipeRefreshLayout? = null
    lateinit var rv : RecyclerView
    lateinit var scrollListener : EndlessRecyclerViewScrollListener
    var sellers: ArrayList<SellerData> = ArrayList()
    private val SEARCH_ADDRESS_ACTIVITY = 10000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v:View = inflater.inflate(R.layout.fragment_search, container, false)

        var mylocate = Location("cur_loc")
        val lon = arguments?.getDouble("address_lon")
        val lat = arguments?.getDouble("address_lat")
        if(lon != null && lat != null){
            mylocate.longitude = lon
            mylocate.latitude = lat
        }
        else {
            mylocate = getMyLocation()
        } // 좌표 가져오기


        var address = App.prefs.address
        if(address == ""){
            address = "주소를 입력해주세요"
        }
        setActionBar(address, mylocate) // 상단바 주소 등록

        val _sellers = arguments?.getSerializable("sellers")!! as ArrayList<SellerData>
        sellers = _sellers

        initRecyclerView(v,mylocate)
        setContent()

        val findMap:ImageView = v.findViewById(R.id.findMap)
        findMap.setOnClickListener {
            activity!!.startActivity<FindToMapActivity>(
                "lat" to mylocate.latitude,
                "lon" to mylocate.longitude)
            (context as MainActivity).overridePendingTransition(R.anim.slide_up, R.anim.slide_stay)
        }

        return v
    }

    fun setActionBar(address:String, mylocate:Location){// 액션 바 설정
        activity!!.titleText.setText(address)
        activity!!.locationIcon.visibility = View.VISIBLE
        activity!!.titleText.setOnClickListener {
            val intent = Intent(context, MyAddressSettingActivity::class.java)
            intent.putExtra("lat",mylocate.latitude)
            intent.putExtra("lon",mylocate.longitude)
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)

            (context as MainActivity).overridePendingTransition(R.anim.slide_up, R.anim.slide_stay)
        }
    }

    override fun onActivityResult( requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY ->
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    val data = intent?.extras!!.getString("address")
                    if (data != null) {
                        activity!!.titleText.setText(data)
                        App.prefs.address = data
                    }
                }
        }
    }

    fun initRecyclerView(v:View, mylocate: Location){

        setRefreshSwipe(v,mylocate)

        rv = v.findViewById(R.id.recyclerViewSearch)

        val linearLayoutManagerWrapper =
            LinearLayoutManagerWrapper(
                activity!!.applicationContext,
                LinearLayoutManager.VERTICAL,
                false
            )

        rv.layoutManager = linearLayoutManagerWrapper

        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManagerWrapper) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                getSeller(v,mylocate,page,false)
            }
        }
        rv.addOnScrollListener(scrollListener)

        val adapter =
            StoreListRecyclerAdapter(sellers)
        rv.adapter = adapter
    }

    fun setContent(){ // 상품 정보들

        rv.adapter?.notifyDataSetChanged()

        while (rv.getItemDecorationCount() > 0) {
            rv.removeItemDecorationAt(0)
        }
        rv.addItemDecoration(
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
        // https://codechacha.com/ko/android-recyclerview/   <- 리사이클러뷰 설명
    }
//https://youngest-programming.tistory.com/209?category=898095 리사이클러뷰 스크롤

    fun getMyLocation():Location {
        val gpsTracker: GpsTracker
        gpsTracker = GpsTracker(activity!!.baseContext)

        val mylat = gpsTracker.getLatitude()
        val mylon = gpsTracker.getLongitude()

        val mylocate = Location("myLoc")
        mylocate.latitude = mylat
        mylocate.longitude = mylon

        return mylocate
    }

    fun setRefreshSwipe(v:View,mylocate: Location){
        swipeContainer = v.findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        swipeContainer?.setOnRefreshListener {
            rv.recycledViewPool.clear()
            getSeller(v,mylocate,0,true)
            scrollListener.resetState()
            swipeContainer?.setRefreshing(false)
        }
        swipeContainer?.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }//https://guides.codepath.com/android/Implementing-Pull-to-Refresh-Guide

    fun getSeller(v:View, mylocate: Location, page: Int,fresh:Boolean) {
        val serverConnect = ServerConnect(activity!!.applicationContext)
        val server = serverConnect.conn()

        server.postSellerRequest(mylocate.latitude, mylocate.longitude, page, -1f).enqueue(object :
            Callback<SellerDataResponse> {
            override fun onFailure(call: Call<SellerDataResponse>?, t: Throwable?) {
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SellerDataResponse>, response: Response<SellerDataResponse>) {
                val success = response.body()!!.success
                val sellerdata = response.body()!!.sellerdata

                if (!success) {
                    Toast.makeText(activity, "새로고침 실패", Toast.LENGTH_SHORT).show()
                } else {
                    if(fresh){
                        sellers.clear()
                    }
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

                    setContent()
                }
            }
        })
    }
}