package com.capstone.androidproject.MainFragment

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
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
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.capstone.androidproject.RecyclerDecoration


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

        var address = App.prefs.address
        if(address == ""){
            address = "주소를 입력해주세요"

            val mylocate = getMyLocation()  // 좌표 가져오기
            App.prefs.lat = mylocate.latitude.toFloat()
            App.prefs.lon = mylocate.longitude.toFloat()
        }
        setActionBar(address) // 상단바 주소 등록

        val _sellers = arguments?.getSerializable("sellers")!! as ArrayList<SellerData>
        sellers = _sellers

        initRecyclerView(v)
        setContent()

        val findMap:ImageView = v.findViewById(R.id.findMap)
        findMap.setOnClickListener {
            activity!!.startActivity<FindToMapActivity>()
            (context as MainActivity).overridePendingTransition(R.anim.slide_up, R.anim.slide_stay)
        }

        return v
    }

    fun setActionBar(address:String){// 액션 바 설정
        activity!!.titleText.setText(address)
        activity!!.titleText.setOnClickListener {
            val intent = Intent(context, MyAddressSettingActivity::class.java)
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)

            (context as MainActivity).overridePendingTransition(R.anim.slide_up, R.anim.slide_stay)
        }
    }

    override fun onActivityResult( requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY ->
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    rv.recycledViewPool.clear()
                    getSeller(0,true)
                    scrollListener.resetState()
                    swipeContainer?.setRefreshing(false)
                    val data = intent?.extras!!.getString("address")
                    if (data != null) {
                        activity!!.titleText.setText(data)
                        App.prefs.address = data
                    }
                }
        }
    }

    fun initRecyclerView(v:View){

        setRefreshSwipe(v)

        rv = v.findViewById(R.id.recyclerViewSearch)

        val linearLayoutManagerWrapper =
            LinearLayoutManagerWrapper(
                activity!!.applicationContext,
                LinearLayoutManager.VERTICAL,
                false
            )

        rv.layoutManager = linearLayoutManagerWrapper

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val findmap=v.findViewById(R.id.findMap) as ImageView
                if(rv.canScrollVertically(-1)){
                    findmap.animate().translationY(findmap.height.toFloat())
                }
                else {
                    findmap.animate().translationY(0.toFloat())
                }
            }
        })

        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManagerWrapper) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                getSeller(page,false)
            }
        }

        rv.addOnScrollListener(scrollListener)

        val adapter =
            StoreListRecyclerAdapter(sellers)
        rv.adapter = adapter

        val spaceDecoration = RecyclerDecoration(20)
        rv.addItemDecoration(spaceDecoration)
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

    fun setRefreshSwipe(v:View){
        swipeContainer = v.findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        swipeContainer?.setOnRefreshListener {
            rv.recycledViewPool.clear()
            getSeller(0,true)
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

    fun getSeller(page: Int,fresh:Boolean) {
        val serverConnect = ServerConnect(activity!!.applicationContext)
        val server = serverConnect.conn()

        val mylocate = Location("cur_loc")
        val lat = App.prefs.lat
        val lon = App.prefs.lon
        mylocate.longitude = lon.toDouble()
        mylocate.latitude = lat.toDouble()

        Log.d("locationtest",mylocate.latitude.toString())
        Log.d("locationtest",mylocate.longitude.toString())

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
                        if(sellerCheck(seller))
                            continue
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

    fun sellerCheck(seller:SellerData):Boolean{
        for(sell in sellers){
            if(sell.name == seller.name)
                return true
        }
        return false
    }
}
