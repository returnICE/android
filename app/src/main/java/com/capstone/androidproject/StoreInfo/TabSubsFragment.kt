package com.capstone.androidproject.StoreInfo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

import com.capstone.androidproject.R
import com.capstone.androidproject.Response.SubItem

class TabSubsFragment(val sublist:MutableList<SubsListRecyclerAdapter.Item>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v=inflater.inflate(R.layout.fragment_tab_subs, container, false)

        setContent(v)

        return v
    }

    fun setContent(v:View){ // 상품 정보들

        val adapter = SubsListRecyclerAdapter(sublist)
        val rv : RecyclerView = v.findViewById(R.id.recyclerViewSubTab)
        rv.adapter = adapter

        rv.addItemDecoration(
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
    }

}
