package com.capstone.androidproject.StoreInfo


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.MainActivity

import com.capstone.androidproject.R
import com.capstone.androidproject.SubPayActivity
import org.jetbrains.anko.startActivity

class TabSubsFragment(val sublist:MutableList<SubsListRecyclerAdapter.Item>) : Fragment() {

    var subId:Int = 0
    private val SUB_REQ_CODE = 10000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v=inflater.inflate(R.layout.fragment_tab_subs, container, false)

        setContent(v)

        val btnSubPay = v.findViewById(R.id.btnSubPay) as ImageView
        btnSubPay.setOnClickListener {
            val intent = Intent(context, SubPayActivity::class.java)
            intent.putExtra("subId",subId)

            startActivityForResult(intent,SUB_REQ_CODE)
        }
        return v
    }

    fun setContent(v:View){ // 상품 정보들

        val adapter = SubsListRecyclerAdapter(sublist)
        val rv : RecyclerView = v.findViewById(R.id.recyclerViewSubTab)
        rv.adapter = adapter

        adapter.setOnItemClickListener(object : SubsListRecyclerAdapter.OnItemClickListener{
            override fun onItemClick(v:View, pos:Int){
                val subid = v.findViewById(R.id.subsId) as TextView
                subId = Integer.parseInt(subid.text.toString())

                Toast.makeText(context,subid.text,Toast.LENGTH_SHORT).show()
            }
        })
        //https://thepassion.tistory.com/301

        rv.addItemDecoration(
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.VERTICAL)
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        when (requestCode) {
            SUB_REQ_CODE ->
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    val data = intent?.extras!!.getString("result")
                    if (data != null) {
                        val i = Intent(context, MainActivity::class.java)

                        startActivity(i)
                    }
                }
        }
    }
}
