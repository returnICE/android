package com.capstone.androidproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.AddressSetting.MyAddressSettingActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import com.capstone.androidproject.MainActivity
import com.capstone.androidproject.Response.ItemData


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val v:View = inflater.inflate(R.layout.fragment_home, container, false)


        val _items = ArrayList<ItemData>()
        _items.add(ItemData("2020-02-18","2020-02-20","광어","싱싱합니다.","30000","10000"))
        _items.add(ItemData("2020-02-15","2020-02-18","우럭","맛있읍니다.","20000","15000"))
        _items.add(ItemData("2020-02-10","2020-02-29","초밥","노맛","15000","5000"))
        _items.add(ItemData("2020-03-01","2020-03-02","도시락","유통기한 지남.","9000","500"))
        _items.add(ItemData("2020-02-29","2020-03-03","생수","물","800","50"))

        val address = arguments?.getString("myAddress")!!
        val items = _items

        setActionBar(address)
        //setMyInfo(v)
        setContent(v, items)

        return v
    }

    fun setActionBar(address:String){// 액션 바 설정
        activity!!.titleText.setText(address)
        activity!!.locationIcon.visibility = View.VISIBLE
        activity!!.titleText.setOnClickListener {
            activity!!.startActivity<MyAddressSettingActivity>()
            (context as MainActivity).overridePendingTransition(R.anim.slide_up, R.anim.slide_stay)
        }
    }

    fun setContent(v:View, items:ArrayList<ItemData>){ // 상품 정보들

        val adapter = MySubItemRecyclerAdapter(items)
        val rv : RecyclerView = v.findViewById(R.id.recyclerViewPopular)
        rv.adapter = adapter

        rv.addItemDecoration(
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
        // https://codechacha.com/ko/android-recyclerview/   <- 리사이클러뷰 설명
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

