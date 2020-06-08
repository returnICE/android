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
import com.capstone.androidproject.Response.MenuData
import com.capstone.androidproject.Response.MenuDataResponse
import com.capstone.androidproject.Response.ServiceData
import com.capstone.androidproject.Response.ServiceDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import kotlinx.android.synthetic.main.activity_b2bservice.*
import kotlinx.android.synthetic.main.activity_service.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class B2BActivity : AppCompatActivity() {

/*
    var menus: ArrayList<MenuData> = ArrayList()
    var MenuName: String = ""
    var Price: String = ""
    var MenuId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b2bservice)

        val sellerId = intent.getStringExtra("sellerId")
        val name = intent.getStringExtra("name")

        BStextStoreName.setText(name)

        getMenuInfo(sellerId)

        BSbtnCertification.setOnClickListener() {
            val intent = Intent(this, AcceptActivity::class.java)
            intent.putExtra("sellerName", "")
            intent.putExtra("serviceName", "")
            intent.putExtra("menuName", MenuName)
            intent.putExtra("menuId", MenuId)
            intent.putExtra("subedId", 0)


            startActivity(intent)
        }
    }


    fun setContent() {
        val adapter = ServiceRecyclerAdapter(menus)
        val rv: RecyclerView = this.findViewById(R.id.recyclerViewService)
        rv.adapter = adapter
        rv.addItemDecoration(
            DividerItemDecoration(this!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
        adapter.setOnItemClickListener(object : ServiceRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(v: View, pos: Int) {
                val price = v.findViewById(R.id.priceClickable) as TextView
                val menuName = v.findViewById(R.id.menuNameClickable) as TextView
                val menuId = v.findViewById(R.id.menuIdClickable) as TextView
                Price = price.text.toString()
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
                Toast.makeText(this@B2BActivity, "통신 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<MenuDataResponse>,
                response: Response<MenuDataResponse>
            ) {
                val success = response.body()!!.success
                val menuItem = response.body()!!.menuItem

                if (!success) {
                    Toast.makeText(this@B2BActivity, "정보가져오기 실패", Toast.LENGTH_SHORT).show()
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
*/
}


