package com.capstone.androidproject.StoreInfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.capstone.androidproject.R

class TabInfoFragment(var info:String?) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v:View = inflater.inflate(R.layout.fragment_tab_info, container, false)

        return v
    }

}

/*
        val adapter = MySubItemRecyclerAdapter(items)
        val rv : RecyclerView = v.findViewById(R.id.recyclerViewPopular)
        rv.adapter = adapter

        rv.addItemDecoration(
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
        // https://codechacha.com/ko/android-recyclerview/   <- 리사이클러뷰 설명
*/