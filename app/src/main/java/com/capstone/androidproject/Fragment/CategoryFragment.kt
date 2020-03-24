package com.capstone.androidproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.HomeTab.ItemRecyclerAdapter
import com.capstone.androidproject.R
import kotlinx.android.synthetic.main.activity_main.*
import com.capstone.androidproject.Response.ItemData

class CategoryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setActionBar()
        val v:View = inflater.inflate(R.layout.fragment_category, container, false)
        var textList = ArrayList<String>()
        setTextList(textList)

        var buttonList = ArrayList<ImageView>()
        setButtonList(buttonList,v)

        var textview : TextView = v.findViewById(R.id.category_text)
        for(i in buttonList){
            i.setOnClickListener {
                textview.text = textList.get(buttonList.indexOf(i))
                initResource(buttonList)
                setResource(i,buttonList.indexOf(i))



                val items = ArrayList<ItemData>()
                items.add(ItemData("2020-02-18","2020-02-20","광어","싱싱합니다.","30000","10000"))
                items.add(ItemData("2020-02-15","2020-02-18","우럭","맛있읍니다.","20000","15000"))
                items.add(ItemData("2020-02-10","2020-02-29","초밥","노맛","15000","5000"))
                items.add(ItemData("2020-03-01","2020-03-02","도시락","유통기한 지남.","9000","500"))
                items.add(ItemData("2020-02-29","2020-03-03","생수","물","800","50"))


                val adapter = ItemRecyclerAdapter(items)
                val rv : RecyclerView = v.findViewById(R.id.recycler)
                rv.adapter = adapter
                rv.addItemDecoration(
                    DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.VERTICAL)
                )

            }
        }
        return v
    }

    fun setTextList(textList : ArrayList<String>){
        textList.add("전체")
        textList.add("치킨 / 피자")
        textList.add("족발 / 보쌈")
        textList.add("일식 / 회")
        textList.add("찜 / 탕 / 찌개")
        textList.add("반찬")
        textList.add("과일")
        textList.add("유제품")
        textList.add("카페 / 빵집")
        textList.add("분식")
        textList.add("야식")
        textList.add("기타")
    }
    fun setButtonList(buttonList : ArrayList<ImageView>, v : View){
        buttonList.add(v.findViewById(R.id.category_0))
        buttonList.add(v.findViewById(R.id.category_1))
        buttonList.add(v.findViewById(R.id.category_2))
        buttonList.add(v.findViewById(R.id.category_3))
        buttonList.add(v.findViewById(R.id.category_4))
        buttonList.add(v.findViewById(R.id.category_5))
        buttonList.add(v.findViewById(R.id.category_6))
        buttonList.add(v.findViewById(R.id.category_7))
        buttonList.add(v.findViewById(R.id.category_8))
        buttonList.add(v.findViewById(R.id.category_9))
        buttonList.add(v.findViewById(R.id.category_10))
        buttonList.add(v.findViewById(R.id.category_11))
    }
    fun setActionBar(){// 액션 바 설정
        activity!!.titleText.setText("카테고리")
        activity!!.locationIcon.visibility = View.GONE
    }
    fun initResource(buttonList : ArrayList<ImageView>){
        buttonList.get(0).setImageResource(R.drawable.category_0)
        buttonList.get(1).setImageResource(R.drawable.category_1)
        buttonList.get(2).setImageResource(R.drawable.category_2)
        buttonList.get(3).setImageResource(R.drawable.category_3)
        buttonList.get(4).setImageResource(R.drawable.category_4)
        buttonList.get(5).setImageResource(R.drawable.category_5)
        buttonList.get(6).setImageResource(R.drawable.category_6)
        buttonList.get(7).setImageResource(R.drawable.category_7)
        buttonList.get(8).setImageResource(R.drawable.category_8)
        buttonList.get(9).setImageResource(R.drawable.category_9)
        buttonList.get(10).setImageResource(R.drawable.category_10)
        buttonList.get(11).setImageResource(R.drawable.category_11)
    }
    fun setResource(button : ImageView, index : Int){
        if(index == 0){
            button.setImageResource(R.drawable.select_category_0)
        }
        if(index == 1){
            button.setImageResource(R.drawable.select_category_1)
        }
        if(index == 2) {
            button.setImageResource(R.drawable.select_category_2)
        }
        if(index == 3){
            button.setImageResource(R.drawable.select_category_3)
        }
        if(index == 4){
            button.setImageResource(R.drawable.select_category_4)
        }
        if(index == 5){
            button.setImageResource(R.drawable.select_category_5)
        }
        if(index == 6){
            button.setImageResource(R.drawable.select_category_6)
        }
        if(index == 7){
            button.setImageResource(R.drawable.select_category_7)
        }
        if(index == 8){
            button.setImageResource(R.drawable.select_category_8)
        }
        if(index == 9){
            button.setImageResource(R.drawable.select_category_9)
        }
        if(index == 10){
            button.setImageResource(R.drawable.select_category_10)
        }
        if(index == 11){
            button.setImageResource(R.drawable.select_category_11)
        }



    }
}
