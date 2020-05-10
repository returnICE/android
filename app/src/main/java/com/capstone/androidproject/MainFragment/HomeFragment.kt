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
        activity!!.locationIcon.visibility = View.GONE
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

/*val mTabLayout:TabLayout = v.findViewById(R.id.layout_tab)
mTabLayout.addTab(mTabLayout.newTab().setText("인기"))
mTabLayout.addTab(mTabLayout.newTab().setText("최신"))
mTabLayout.addTab(mTabLayout.newTab().setText("마감 임박"))

val mViewPager : ViewPager = v.findViewById(R.id.pager_content)
val mContentsPagerAdapter = ContentsPagerAdapter(
    childFragmentManager!!, mTabLayout.tabCount
)
mViewPager.setAdapter(mContentsPagerAdapter)

mViewPager.addOnPageChangeListener(
    TabLayout.TabLayoutOnPageChangeListener(mTabLayout)
)

mTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab) {
        mViewPager.currentItem = tab.position
    }
    override fun onTabUnselected(tab: TabLayout.Tab) {
    }
    override fun onTabReselected(tab: TabLayout.Tab) {
    }
})*/ //탭레이아웃 예시

// 탭레이아웃(TabLayout), 뷰페이저(ViewPager) 사용
// https://re-build.tistory.com/25
// https://recipes4dev.tistory.com/147

