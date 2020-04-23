package com.capstone.androidproject.StoreInfo

import android.os.AsyncTask
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.os.Parcelable
import com.capstone.androidproject.Response.MenuData
import com.capstone.androidproject.Response.SellerData
import com.capstone.androidproject.Response.SellerInfoResponse
import com.capstone.androidproject.Response.SubItem
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.StoreActivity
import java.io.IOException
import java.lang.ref.WeakReference

class StoreInfoTabAdapter(fm: FragmentManager, pageCount : Int,
                          var sublist:MutableList<SubsListRecyclerAdapter.Item>,
                          val menudata:ArrayList<MenuData>,
                          var info:String ?= "hi") : FragmentStatePagerAdapter(fm){
    var mPageCount:Int = pageCount

    override fun getItem(position: Int): Fragment {

        // 서버에서 item,항공,숙박 받아오는 작업 필요
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