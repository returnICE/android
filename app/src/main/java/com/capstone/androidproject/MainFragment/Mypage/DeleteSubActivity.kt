package com.capstone.androidproject.MainFragment.Mypage

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.MainFragment.MypageFragment
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.EatenLogData
import com.capstone.androidproject.Response.EatenLogDataResponse
import com.capstone.androidproject.Response.EatenLogDataResponse2
import com.capstone.androidproject.Response.SubedItemData
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.ServiceInfo.ServiceRecyclerAdapter
import com.capstone.androidproject.SharedPreferenceConfig.App
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteSubActivity: AppCompatActivity() {

    var subeds: ArrayList<SubedItemData> = ArrayList()
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
        val lm = LinearLayoutManager(this@DeleteSubActivity, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = lm

        Log.d("testing","EatenLogActivity - recycler test")
    }

}