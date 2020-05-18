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
import com.capstone.androidproject.MainFragment.Home.HomeRecyclerAdapter
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.SubedItemData
import kotlinx.android.synthetic.main.activity_main.*



class HomeFragment : Fragment() {

    var subeds: ArrayList<SubedItemData> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v:View = inflater.inflate(R.layout.fragment_home, container, false)

        setActionBar()

        val _subeds = arguments?.getSerializable("subeds")!! as ArrayList<SubedItemData>
        subeds = _subeds
        Log.d("test", "subeds Home" + subeds)
        setContent(v, subeds)
        Log.d("test", "subeds Home2" + subeds)
        return v
    }

    fun setActionBar(){// 액션 바 설정
        activity!!.titleText.setText("구독 목록")
        activity!!.titleText.isClickable = false
    }
    fun setContent(v:View, subeds:ArrayList<SubedItemData>) {

        val adapter = HomeRecyclerAdapter(subeds)
        val rv: RecyclerView = v.findViewById(R.id.recyclerViewHome)
        Toast.makeText(context, "리사이클러뷰 테스트", Toast.LENGTH_SHORT).show()
        rv.adapter = adapter
        rv.addItemDecoration(
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.layoutManager = lm
    }

}

