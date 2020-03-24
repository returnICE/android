package com.capstone.androidproject.HomeTab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

import com.capstone.androidproject.R
import com.capstone.androidproject.Response.ItemData

class TabPopularFragment(_items:ArrayList<ItemData>) : Fragment() {

    val items = _items
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v:View = inflater.inflate(R.layout.fragment_tab_popular, container, false)

        val adapter = ItemRecyclerAdapter(items)
        val rv : RecyclerView = v.findViewById(R.id.recyclerViewPopular)
        rv.adapter = adapter

        rv.addItemDecoration(
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
        // https://codechacha.com/ko/android-recyclerview/   <- 리사이클러뷰 설명

        return v
    }

}
