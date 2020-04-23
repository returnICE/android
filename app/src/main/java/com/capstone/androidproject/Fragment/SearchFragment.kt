package com.capstone.androidproject.Fragment

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.capstone.androidproject.AddressSetting.GpsTracker
import com.capstone.androidproject.AddressSetting.MyAddressSettingActivity
import com.capstone.androidproject.MainActivity
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.SellerData
import com.capstone.androidproject.Response.SellerDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {

    private var swipeContainer: SwipeRefreshLayout? = null
    lateinit var rv : RecyclerView
    lateinit var scrollListener : EndlessRecyclerViewScrollListener
    var sellers: ArrayList<SellerData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v:View = inflater.inflate(R.layout.fragment_search, container, false)

        val address = arguments?.getString("myAddress")!!
        setActionBar(address)

        val mylocate = getMyLocation()

        val _sellers = arguments?.getSerializable("sellers")!! as ArrayList<SellerData>
        sellers = _sellers

        initRecyclerView(v,mylocate)
        setContent()

        return v
    }

    fun setActionBar(address:String){// 액션 바 설정
        activity!!.titleText.setText(address)
        activity!!.locationIcon.visibility = View.VISIBLE
        activity!!.titleText.setOnClickListener {
            activity!!.startActivity<MyAddressSettingActivity>()
            (context as MainActivity).overridePendingTransition(R.anim.slide_up, R.anim.slide_stay)
        }
    }

    fun initRecyclerView(v:View, mylocate: Location){

        setRefreshSwipe(v,mylocate)

        rv = v.findViewById(R.id.recyclerViewSearch)

        val linearLayoutManagerWrapper = LinearLayoutManagerWrapper(activity!!.applicationContext, LinearLayoutManager.VERTICAL, false)

        rv.layoutManager = linearLayoutManagerWrapper

        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManagerWrapper) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                getSeller(v,mylocate,page,false)
            }
        }
        rv.addOnScrollListener(scrollListener)

        val adapter = StoreListRecyclerAdapter(sellers)
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

        server.postSellerRequest(mylocate.latitude, mylocate.longitude, page).enqueue(object :
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
