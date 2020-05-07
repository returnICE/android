package com.capstone.androidproject.MainFragment

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.LoginActivity
import com.capstone.androidproject.MainFragment.Mypage.MypageRecyclerAdapter
import com.capstone.androidproject.MainFragment.StoreList.EndlessRecyclerViewScrollListener
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.SellerData
import com.capstone.androidproject.Response.SellerDataResponse
import com.capstone.androidproject.Response.Success
import com.capstone.androidproject.Response.UserInfoResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.capstone.androidproject.Response.SubedItemData as SubedItemData

class MypageFragment() : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    //SellerData -> SubedItemData
    var subedItemData = arrayListOf<SellerData>(

        SellerData("","","","",0,0.0,0.0,0.0,"","","",0),
        SellerData("","","","",0,0.0,0.0,0.0,"","","",0),
        SellerData("","","","",0,0.0,0.0,0.0,"","","",0),
        SellerData("","","","",0,0.0,0.0,0.0,"","","",0),
        SellerData("","","","",0,0.0,0.0,0.0,"","","",0),
        SellerData("","","","",0,0.0,0.0,0.0,"","","",0)

    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v:View = inflater.inflate(R.layout.fragment_mypage, container, false)

        setActionBar()

        var btnlogout: ImageView = v.findViewById(R.id.btnLogout)
        btnlogout.setOnClickListener {
            App.prefs.clear()
            logout(context!!.applicationContext)
        }

        getname(context!!.applicationContext)

        setContent(v)

        return v
    }

    fun setActionBar(){// 액션 바 설정
        activity!!.titleText.setText("마이페이지")
        activity!!.locationIcon.visibility = View.GONE
        activity!!.titleText.isClickable = false
    }

    fun logout(ctx:Context) {
        val serverConnect = ServerConnect(ctx)
        val server = serverConnect.conn()

        server.getLogoutRequest().enqueue(object : Callback<Success> {
            override fun onFailure(call: Call<Success>?, t: Throwable?) {
                Toast.makeText(ctx, "로그아웃 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Success>?, response: Response<Success>?) {
                val succ = response?.body()

                if (succ?.success == false) {
                    Toast.makeText(ctx, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(ctx, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                    activity?.startActivity<LoginActivity>()
                    activity?.finish()
                }
            }
        })
    }

    fun getname(ctx: Context){
        val serverConnect = ServerConnect(ctx)
        val server = serverConnect.conn()

        Log.d("test", "token Mypage : " + App.prefs.data)
        server.getCustomerInfoRequest(App.prefs.data).enqueue(object : Callback<UserInfoResponse>{
            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Toast.makeText(ctx, "사용자 이름 받아오기 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                val succ = response.body()?.success
                val userinfo = response.body()?.data

                if (succ == false) {
                    Toast.makeText(ctx, "사용자 이름 받아오기 실패 2", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(ctx, "사용자 이름 받아오기 성공", Toast.LENGTH_SHORT).show()
                    Log.d("test", "name Mypage : " + userinfo?.name)
                    textMypageName.setText(userinfo?.name + "님")

                }
            }

        })

    }

    fun setContent(v:View){

        val adapter = MypageRecyclerAdapter(subedItemData)
        val rv : RecyclerView = v.findViewById(R.id.recyclerViewMypage)
        Toast.makeText(context, "리사이클려뷰 테스트", Toast.LENGTH_SHORT).show()
        rv.adapter = adapter
        rv.addItemDecoration(
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.HORIZONTAL)
        )
        val lm = LinearLayoutManager(context,HORIZONTAL, false)
        rv.layoutManager =  lm
/*
    fun getSeller(v:View, mylocate: Location, page: Int, fresh:Boolean) {
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
                        subedItemData.clear()
                    }
                    for (seller in sellerdata) {
                        val sellerlocate = Location("myLoc")
                        sellerlocate.longitude = seller.lon
                        sellerlocate.latitude = seller.lat
                        val distance = mylocate.distanceTo(sellerlocate).toDouble()

                        subedItemData.add(
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
                    subedItemData.sortWith(compareBy({ it.distance }))

                    setContent(v)
                }
            }
        })
        */
    }
}
