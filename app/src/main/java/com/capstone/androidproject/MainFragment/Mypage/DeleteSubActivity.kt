package com.capstone.androidproject.MainFragment.Mypage

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
import com.capstone.androidproject.MainActivity
import com.capstone.androidproject.MainFragment.MypageFragment
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.*
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.ServiceInfo.ServiceRecyclerAdapter
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_delete_sub.*
import kotlinx.android.synthetic.main.item_view_mypage_subeditem.*
import kotlinx.android.synthetic.main.item_view_service.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteSubActivity: AppCompatActivity() {

    var subeds: ArrayList<SubedItemData> = ArrayList()
    var SubedId : String = ""
    var AutoPay : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_sub)

        val test = intent.getStringExtra("test")
        Log.d("testing","test dsa : "+test)
        val _subeds = intent.getSerializableExtra("subeds") as ArrayList<SubedItemData>
        subeds = _subeds
        Log.d("testing", "subeds???? " + subeds)
        setContent(subeds)

    }
    fun setContent(subeds:ArrayList<SubedItemData>) {

        val adapter = DeleteSubRecyclerAdapter(subeds)
        val rv : RecyclerView = this.findViewById(R.id.recyclerViewDeleteSub)
        rv.adapter = adapter
        rv.addItemDecoration(
            DividerItemDecoration(this!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
        adapter.setOnItemClickListener(object: DeleteSubRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(v: View, pos: Int) {
                val subedId = v.findViewById(R.id.IVSsubedId) as TextView
                val autoPay = v.findViewById(R.id.IVSautoPay) as TextView
                SubedId = subedId.text.toString()
                AutoPay = autoPay.text.toString()
                Log.d("testing", "SubedId in DSA : " + SubedId)
            }
        })
        val lm = LinearLayoutManager(this@DeleteSubActivity, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = lm

        ADSbtnCertification.setOnClickListener(){
            if(AutoPay == "1"){
                Log.d("testing", "SubedId in DSA2 : " + Integer.parseInt(SubedId).toString())
                updateAutoPay(App.prefs.token, Integer.parseInt(SubedId))
            }
            else{
                Toast.makeText(this@DeleteSubActivity, "해지할 서비스를 선택해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateAutoPay(token: String, subedId: Int) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.putCustomerAutoPayRequest(token, subedId).enqueue(object : Callback<Success> {
            override fun onFailure(call: Call<Success>?, t: Throwable?) {
                Toast.makeText(this@DeleteSubActivity, "변결 실패1", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Success>?, response: Response<Success>) {
                val success = response.body()?.success

                if (success == false) {
                    Toast.makeText(this@DeleteSubActivity, "변경 실패2", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DeleteSubActivity, "자동결제 변경 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@DeleteSubActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }
}