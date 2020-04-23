package com.capstone.androidproject.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.androidproject.R
import kotlinx.android.synthetic.main.activity_main.*



class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v:View = inflater.inflate(R.layout.fragment_home, container, false)

        setActionBar()

        return v
    }

    fun setActionBar(){// 액션 바 설정
        activity!!.titleText.setText("구독 목록")
        activity!!.locationIcon.visibility = View.GONE
        activity!!.titleText.isClickable = false
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

