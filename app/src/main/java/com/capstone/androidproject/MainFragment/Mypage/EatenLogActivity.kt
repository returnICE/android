package com.capstone.androidproject.MainFragment.Mypage

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.EatenLogData
import com.capstone.androidproject.Response.EatenLogDataResponse2
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EatenLogActivity: AppCompatActivity() {
    var eatenLogs: ArrayList<EatenLogData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eatenlog)
        getLog()

    }
    fun setContent(eatenLogs:ArrayList<EatenLogData>) {

        val adapter = EatenLogRecyclerAdapter(eatenLogs)
        val rv : RecyclerView = this.findViewById(R.id.recyclerViewEatenLog)
        rv.adapter = adapter
        rv.addItemDecoration(
            DividerItemDecoration(this!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
        val lm = LinearLayoutManager(this@EatenLogActivity, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = lm

    }

    fun getLog(){
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.getEatenLogInfoRequest(App.prefs.token).enqueue(object : Callback<EatenLogDataResponse2> {
            override fun onFailure(call: Call<EatenLogDataResponse2>?, t: Throwable?) {
                Toast.makeText(this@EatenLogActivity, "목록 가져오기 실패1", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<EatenLogDataResponse2>?, response: Response<EatenLogDataResponse2>) {
                val success = response.body()?.success
                val logData = response.body()!!.data

                if (success == false) {
                    Toast.makeText(this@EatenLogActivity, "목록 가져오기 실패2", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@EatenLogActivity, "목록 가져오기 성공", Toast.LENGTH_SHORT).show()
                    for (eatenLog in logData) {
                        eatenLogs.add(
                            EatenLogData(
                                eatenLog.eatenId,
                                eatenLog?.eatenDate,
                                eatenLog?.score,
                                eatenLog.enterpriseId,
                                eatenLog.menuName,
                                eatenLog.price
                            )
                        )
                    }
                    setContent(eatenLogs)
                }
            }
        })
    }
}