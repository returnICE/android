package com.capstone.androidproject.MainFragment.Mypage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.ServiceInfo.ServiceRecyclerAdapter

class EatenLogActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eatenlog)

        setContent()
    }
    fun setContent() {



        val adapter = EatenLogRecyclerAdapter()
        val rv : RecyclerView = this.findViewById(R.id.recyclerViewEatenLog)
        rv.adapter = adapter
        rv.addItemDecoration(
            DividerItemDecoration(this!!.applicationContext, DividerItemDecoration.HORIZONTAL)
        )
        val lm = LinearLayoutManager(this@EatenLogActivity, LinearLayoutManager.HORIZONTAL, false)
        rv.layoutManager = lm
    }
}