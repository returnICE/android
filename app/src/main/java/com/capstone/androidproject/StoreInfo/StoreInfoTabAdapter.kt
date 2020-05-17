package com.capstone.androidproject.StoreInfo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.os.Parcelable
import com.capstone.androidproject.Response.MenuData

class StoreInfoTabAdapter(fm: FragmentManager, pageCount : Int,
                          var sublist:MutableList<SubsListRecyclerAdapter.Item>,
                          val menudata:ArrayList<MenuData>,
                          var info:String ?= "hi") : FragmentStatePagerAdapter(fm){
    var mPageCount:Int = pageCount

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                return TabSubsFragment(sublist)
            }
            1 -> {
                return TabMenuFragment(menudata)
            }
            2 -> {
                return TabInfoFragment(info)
            }
            else -> return TabSubsFragment(sublist)
        }
    }

    override fun getCount(): Int {
        return mPageCount
    }
    override fun saveState(): Parcelable? {
        return null
    }

}