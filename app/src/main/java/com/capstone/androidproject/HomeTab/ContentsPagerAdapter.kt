package com.capstone.androidproject.HomeTab

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.capstone.androidproject.Fragment.HomeFragment
import com.capstone.androidproject.Response.ItemData
import android.os.Parcelable

class ContentsPagerAdapter(fm: FragmentManager, pageCount : Int) : FragmentStatePagerAdapter(fm){
    var mPageCount:Int = pageCount

    override fun getItem(position: Int): Fragment {

        val items = ArrayList<ItemData>()
        items.add(ItemData("2020-02-18","2020-02-20","광어","싱싱합니다.","30000","10000"))
        items.add(ItemData("2020-02-15","2020-02-18","우럭","맛있읍니다.","20000","15000"))
        items.add(ItemData("2020-02-10","2020-02-29","초밥","노맛","15000","5000"))
        items.add(ItemData("2020-03-01","2020-03-02","도시락","유통기한 지남.","9000","500"))
        items.add(ItemData("2020-02-29","2020-03-03","생수","물","800","50"))
        // 서버에서 item,항공,숙박 받아오는 작업 필요

        when (position) {
            0 -> {
                return TabPopularFragment(items)
            }
            1 -> {
                return TabNewFragment()
            }
            2 -> {
                return TabDeadlineFragment()
            }
            else -> return TabPopularFragment(items)
        }
    }

    override fun getCount(): Int {
        return mPageCount
    }
    override fun saveState(): Parcelable? {
        return null
    }
}