package com.capstone.androidproject.MainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.capstone.androidproject.MainFragment.Home.HomeRecyclerAdapter
import com.capstone.androidproject.R
import com.capstone.androidproject.RecyclerDecoration
import com.capstone.androidproject.Response.SubedItemData
import com.capstone.androidproject.Response.SubedItmeDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {


    private var swipeContainer: SwipeRefreshLayout? = null
    lateinit var rv : RecyclerView
    var subeds: ArrayList<SubedItemData> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v:View = inflater.inflate(R.layout.fragment_home, container, false)

        setActionBar()

        val _subeds = arguments?.getSerializable("subeds")!! as ArrayList<SubedItemData>
        subeds = _subeds
        initRecyclerView(v, subeds)
        return v
    }

    fun setActionBar(){// 액션 바 설정
        activity!!.titleText.setText("구독 목록")
        activity!!.titleText.isClickable = false
    }
    fun initRecyclerView(v:View, subeds:ArrayList<SubedItemData>) {

        setRefreshSwipe(v)

        rv = v.findViewById(R.id.recyclerViewHome)
        val adapter = HomeRecyclerAdapter(subeds)
        rv.adapter = adapter

        val spaceDecoration = RecyclerDecoration(activity!!, 8)
        rv.addItemDecoration(spaceDecoration)

        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = lm
    }

    fun setRefreshSwipe(v:View){
        swipeContainer = v.findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        swipeContainer?.setOnRefreshListener {
            rv.recycledViewPool.clear()
            getSubedItem()
            swipeContainer?.setRefreshing(false)
        }
        swipeContainer?.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    fun setContent(){ // 상품 정보들

        rv.adapter?.notifyDataSetChanged()

        while (rv.getItemDecorationCount() > 0) {
            rv.removeItemDecorationAt(0)
        }
        val spaceDecoration = RecyclerDecoration(activity!!, 8)
        rv.addItemDecoration(spaceDecoration)
        // https://codechacha.com/ko/android-recyclerview/   <- 리사이클러뷰 설명
    }


    fun getSubedItem(){
        val serverConnect = ServerConnect(activity!!.applicationContext)
        val server = serverConnect.conn()

        server.getSubedItemRequest(App.prefs.token).enqueue(object:
            Callback<SubedItmeDataResponse> {
            override fun onFailure(call: Call<SubedItmeDataResponse>, t: Throwable) {
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<SubedItmeDataResponse>,
                response: Response<SubedItmeDataResponse>
            ) {
                val success = response.body()!!.success
                val subdata = response.body()!!.subdata

                if(!success){
                    Toast.makeText(activity, "목록가져오기 실패", Toast.LENGTH_SHORT).show()
                }
                else{
                    subeds.clear()
                    for (subed in subdata) {
                        if(subeds.indexOf(subed) != -1)
                            continue
                        subeds.add(
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

                    setContent()
                }
            }

        })

    }

}

