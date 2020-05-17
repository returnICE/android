package com.capstone.androidproject.StoreInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.*
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SubPayActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_store.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreActivity : AppCompatActivity() {

    lateinit var sellerdetaildata: SellerData
    var menudata:ArrayList<MenuData> = ArrayList()
    var subItem:ArrayList<SubItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val sellerId = intent.getStringExtra("sellerId")
        val name = intent.getStringExtra("name")

        getSellerInfo(sellerId)


    }

    fun setContent(){

        val mTabLayout: TabLayout = findViewById(R.id.layout_tab)
        mTabLayout.addTab(mTabLayout.newTab().setText("구독 상품"))
        mTabLayout.addTab(mTabLayout.newTab().setText("메뉴"))
        mTabLayout.addTab(mTabLayout.newTab().setText("정보"))

        var sublist:MutableList<SubsListRecyclerAdapter.Item> = ArrayList()

        for(s in subItem){
            var subinfo = SubInfo()
            subinfo.subId = s.subId
            subinfo.subName = s.subName
            subinfo.price = s.price
            subinfo.limitTimes = s.limitTimes
            subinfo.term = s.term
            subinfo.info = s.info
            sublist.add(SubsListRecyclerAdapter.Item(SubsListRecyclerAdapter.HEADER,subinfo))

            for(m in s.menu){
                var menuinfo = MenuInfo()
                menuinfo.menuName = m.menuName
                menuinfo.price = m.price
                menuinfo.avgScore = m.avgScore
                sublist.add(SubsListRecyclerAdapter.Item(SubsListRecyclerAdapter.CHILD,menuinfo))
            }
        }

        val mViewPager : ViewPager = findViewById(R.id.pager_content)
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
    }

    fun getSellerInfo(sellerId:String){

        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getSellerInfoRequest(sellerId).enqueue(object :Callback<SellerInfoResponse> {
            override fun onFailure(call: Call<SellerInfoResponse>?, t: Throwable?) {
                Toast.makeText(this@StoreActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SellerInfoResponse>, response: Response<SellerInfoResponse>) {
                val success = response.body()!!.success
                val sellerinfo = response.body()!!

                if (!success) {
                    Toast.makeText(this@StoreActivity, "정보가져오기 실패", Toast.LENGTH_SHORT).show()
                } else {
                    sellerdetaildata = sellerinfo.sellerdetaildata
                    menudata = sellerinfo.menudata
                    subItem = sellerinfo.subItem

                    storeName.setText(sellerdetaildata.name)

                    setContent()
                }
            }
        })
    }

    data class SubInfo(
        var subId:Int=0,
        var subName:String="",
        var price:Int=0,
        var limitTimes:Int=0,
        var term:Int=0,
        var info:String?=""
    )

    data class MenuInfo(
        var menuName:String="",
        var price:Int=0,
        var avgScore:Double=0.0
    )

}
