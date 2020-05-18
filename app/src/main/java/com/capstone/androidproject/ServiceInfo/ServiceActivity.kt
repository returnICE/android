package com.capstone.androidproject.ServiceInfo


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.AcceptRequest.AcceptActivity
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.ServiceData
import com.capstone.androidproject.Response.ServiceDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import kotlinx.android.synthetic.main.activity_service.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceActivity : AppCompatActivity() {

    lateinit var subItem: ServiceData
    private val SUBED_REQ_CODE = 9000
    var MenuName:String = ""
    var Price:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("test","11")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)

        val subedId = intent.getIntExtra("subedId",0)
        Log.d("testing", "subedId?")
        val name = intent.getStringExtra("name")

        SEtextStoreName.setText(name)

        getServiceInfo(subedId)

        SEbtnCertification.setOnClickListener(){
            val intent = Intent(this, AcceptActivity::class.java)
            intent.putExtra("sellerName",subItem.name)
            intent.putExtra("serviceName", subItem.subName)
            intent.putExtra("menuName", MenuName)
            intent.putExtra("price", Price)


            startActivityForResult(intent,SUBED_REQ_CODE)
        }

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
        adapter.setOnItemClickListener(object : ServiceRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos:Int){
                val price = v.findViewById(R.id.price) as TextView
                val menuName = v.findViewById(R.id.menuName) as TextView
                Price = price.text.toString()
                MenuName = menuName.text.toString()

            }
        })
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        when (requestCode) {
            SUBED_REQ_CODE ->
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    val data = intent?.extras!!.getString("result")
                    if (data != null) {
                        val i = Intent(this, AcceptActivity::class.java)

                        startActivity(i)
                    }
                }
        }
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

