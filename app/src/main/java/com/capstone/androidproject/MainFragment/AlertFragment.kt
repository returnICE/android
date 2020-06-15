package com.capstone.androidproject.MainFragment

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
import com.capstone.androidproject.MainFragment.Alert.AlertRecyclerAdapter
import com.capstone.androidproject.R
import com.capstone.androidproject.RecyclerDecoration
import com.capstone.androidproject.Response.CampaignData
import com.capstone.androidproject.Response.CampaignDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlertFragment : Fragment() {

    private var swipeContainer: SwipeRefreshLayout? = null
    lateinit var rv : RecyclerView
    var alerts: ArrayList<CampaignData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v:View = inflater.inflate(R.layout.fragment_alert, container, false)

        setActionBar()

        val _alerts = arguments?.getSerializable("alerts")!! as ArrayList<CampaignData>
        alerts = _alerts
        initRecyclerView(v, alerts)

        return v
    }

    fun setActionBar(){// 액션 바 설정
        activity!!.titleText.setText("알람")
        activity!!.titleText.isClickable = false
    }

    fun initRecyclerView(v:View, subeds:ArrayList<CampaignData>) {

        setRefreshSwipe(v)
        getCampaignItem()

        rv = v.findViewById(R.id.recyclerViewAlert)
        val adapter = AlertRecyclerAdapter(subeds)
        rv.adapter = adapter

        //val spaceDecoration = RecyclerDecoration(activity!!, 8)
        rv.addItemDecoration(object: DividerItemDecoration(v.context,1){})

        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = lm
    }

    fun setRefreshSwipe(v:View){
        swipeContainer = v.findViewById(R.id.swipeContainer) as SwipeRefreshLayout
        swipeContainer?.setOnRefreshListener {
            rv.recycledViewPool.clear()
            getCampaignItem()
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
        //val spaceDecoration = RecyclerDecoration(activity!!, 8)
        rv.addItemDecoration(object: DividerItemDecoration(context,1){})
        // https://codechacha.com/ko/android-recyclerview/   <- 리사이클러뷰 설명
    }

    fun getCampaignItem(){
        val serverConnect = ServerConnect(activity!!.applicationContext)
        val server = serverConnect.conn()

        server.getCampaignItemRequest(App.prefs.token).enqueue(object:
            Callback<CampaignDataResponse> {
            override fun onFailure(call: Call<CampaignDataResponse>, t: Throwable) {
                Toast.makeText(activity, "통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CampaignDataResponse>,
                response: Response<CampaignDataResponse>
            ) {
                val success = response.body()!!.success
                val campaignlist = response.body()!!.campaign

                if(!success){
                    Toast.makeText(activity, "목록가져오기 실패", Toast.LENGTH_SHORT).show()
                }
                else{
                    alerts.clear()
                    for (alert in campaignlist) {
                        alerts.add(
                            CampaignData(
                                alert.customerId,
                                alert.campaignId,
                                alert.ccId,
                                alert.campaign
                            )
                        )
                    }
                    alerts.sortWith(compareBy({it.campaign.transmitDate}))
                    setContent()
                }
            }
        })
    }


}
