package com.capstone.androidproject.ServiceInfo


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.ServiceData
import com.capstone.androidproject.Response.ServiceDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import kotlinx.android.synthetic.main.activity_service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceActivity : AppCompatActivity() {

    lateinit var subItem: ServiceData

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("test","11")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        val subedId = intent.getIntExtra("subedId",0)
        Log.d("testing", "subedId?")
        val name = intent.getStringExtra("name")

        SMtext_storeName.setText(name)


        getServiceInfo(subedId)
    }

    fun setContent() {


        var sublist: MutableList<ServiceRecyclerAdapter.Item> = ArrayList()

        var subinfo = SubInfo()
        subinfo.endDate = subItem.endDate
        subinfo.usedTimes = subItem.usedTimes
        subinfo.price = subItem.price
        subinfo.sellerId = subItem.sellerId
        subinfo.subId = subItem.subId
        subinfo.term = subItem.term
        subinfo.name = subItem.name
        subinfo.limitTimes = subItem.limitTimes
        subinfo.subName = subItem.subName
        Log.d("testing", "subInfo??" + subinfo.toString())
        sublist.add(ServiceRecyclerAdapter.Item(ServiceRecyclerAdapter.HEADER, subinfo))

        for (m in subItem.menu) {
            var menuinfo = MenuInfo()
            menuinfo.menuName = m.menuName
            menuinfo.price = m.price
            menuinfo.avgScore = m.avgScore
            sublist.add(ServiceRecyclerAdapter.Item(ServiceRecyclerAdapter.CHILD, menuinfo))
        }

        val adapter = ServiceRecyclerAdapter(sublist)
        val rv : RecyclerView = this.findViewById(R.id.recyclerViewService)
        rv.adapter = adapter
        rv.addItemDecoration(
            DividerItemDecoration(this!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
        val lm = LinearLayoutManager(this@ServiceActivity, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = lm
    }


    /*
    val mViewPager: ViewPager = findViewById(R.id.SMpager_content)
    val mContentsPagerAdapter = StoreInfoTabAdapter(
        supportFragmentManager, mTabLayout.tabCount, sublist, menudata, sellerdetaildata.info
    )
    mViewPager.setAdapter(mContentsPagerAdapter)

    mViewPager.addOnPageChangeListener(
        TabLayout.TabLayoutOnPageChangeListener(mTabLayout)
    )

    mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            mViewPager.currentItem = tab.position
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
        }
    })

    }*/

    fun getServiceInfo(subedId: Int) {

        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getServiceRequest(subedId).enqueue(object : Callback<ServiceDataResponse> {
            override fun onFailure(call: Call<ServiceDataResponse>?, t: Throwable?) {
                Toast.makeText(this@ServiceActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ServiceDataResponse>,
                response: Response<ServiceDataResponse>
            ) {
                val success = response.body()!!.success
                val subedItemInfo = response.body()!!.subedItem

                if (!success) {
                    Toast.makeText(this@ServiceActivity, "정보가져오기 실패", Toast.LENGTH_SHORT).show()
                } else {
                    subItem = subedItemInfo[0]
                    Log.d("testing", "subItem??" + subItem.toString())

                    setContent()
                }
            }
        })
    }


    data class SubInfo(
        var endDate: String = "",
        var term: Int = 0,
        var limitTimes: Int = 0,
        var usedTimes: Int = 0,
        var subId: Int = 0,
        var subName: String = "",
        var sellerId: String = "",
        var price: Int = 0,
        var name: String = ""
    )


    data class MenuInfo(
        var menuName: String = "",
        var price: Int = 0,
        var avgScore: Double = 0.0
    )

}

