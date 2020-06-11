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
import com.capstone.androidproject.Response.B2BData
import com.capstone.androidproject.Response.MenuData
import com.capstone.androidproject.Response.MenuDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_b2bservice.*
import org.jetbrains.anko.db.INTEGER
import org.jetbrains.anko.db.IntParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class B2BActivity : AppCompatActivity() {

    var menus: ArrayList<MenuData> = ArrayList()
    var MenuName: String = ""
    var Price: Int = 0
    var MenuId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b2bservice)

        val sellerId = intent.getStringExtra("sellerId")
        val name = intent.getStringExtra("name")

        BStextStoreName.setText(name)
        val restAmountPerDay = App.prefs.amountPerDay - App.prefs.usedAmountPerDay
        val restAmountPerMonth = App.prefs.amountPerMonth - App.prefs.usedAmountPerMonth
        Log.d("testing", "apd in b2bactivity : " + restAmountPerDay.toString())
        BSrestAmountPerDay.setText("오늘 남은 잔액 : " + restAmountPerDay.toString())
        BSrestAmountPerMonth.setText("이번 달 남은 잔액 : " + restAmountPerMonth.toString())
        BSresetDate.setText("초기화 날짜 : " + App.prefs.resetDate)
        getMenuInfo(sellerId)

        BSbtnCertification.setOnClickListener() {
            if(restAmountPerDay > Price && restAmountPerMonth > Price) {
                val intent = Intent(this, AcceptActivity::class.java)
                intent.putExtra("sellerName", name)
                intent.putExtra("serviceName", "")
                intent.putExtra("menuName", MenuName)
                intent.putExtra("menuId", MenuId)
                intent.putExtra("subedId", 0)
                intent.putExtra("isB2B", 1)
                intent.putExtra("price", Price)
                startActivity(intent)
            }
            else{
                Toast.makeText(this@B2BActivity, "잔액이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun setContent() {
        val adapter = B2BRecyclerAdapter(menus)
        val rv: RecyclerView = this.findViewById(R.id.recyclerViewB2B)
        rv.adapter = adapter
        rv.addItemDecoration(
            DividerItemDecoration(this!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
        adapter.setOnItemClickListener(object : B2BRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(v: View, pos: Int) {
                val price = v.findViewById(R.id.priceClickable) as TextView
                val menuName = v.findViewById(R.id.menuNameClickable) as TextView
                val menuId = v.findViewById(R.id.menuIdClickable) as TextView
                Price = Integer.parseInt(price.text.toString())
                MenuName = menuName.text.toString()
                MenuId = menuId.text.toString()
            }
        })
        val lm = LinearLayoutManager(this@B2BActivity, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = lm
    }

    fun getMenuInfo(sellerId: String) {

        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getMenuRequest(sellerId).enqueue(object : Callback<MenuDataResponse> {
            override fun onFailure(call: Call<MenuDataResponse>?, t: Throwable?) {
                Toast.makeText(this@B2BActivity, "getMenuInfo 통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<MenuDataResponse>,
                response: Response<MenuDataResponse>
            ) {
                val success = response.body()!!.success
                val menuItem = response.body()!!.menuItem

                if (!success) {
                    Toast.makeText(this@B2BActivity, "getMenuInfo 정보가져오기 실패", Toast.LENGTH_SHORT).show()
                } else {
                    menus.clear()
                    for (menu in menuItem) {
                        if (menus.indexOf(menu) != -1)
                            continue
                        menus.add(
                            MenuData(
                                menu.menuId,
                                menu.menuName,
                                menu.price,
                                menu.avgScore
                            )
                        )
                    }
                    setContent()
                }
            }
        })
    }
    data class MenuInfo(
        var menuName: String = "",
        var price: Int = 0,
        var avgScore: Double = 0.0,
        var menuId: Int = 0
    )

}


