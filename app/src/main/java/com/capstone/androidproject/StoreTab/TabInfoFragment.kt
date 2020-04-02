package com.capstone.androidproject.StoreTab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.capstone.androidproject.R
import com.capstone.androidproject.Response.ItemData

class TabInfoFragment(_items:ArrayList<ItemData>) : Fragment() {

    val items = _items
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v:View = inflater.inflate(R.layout.fragment_tab_popular, container, false)

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