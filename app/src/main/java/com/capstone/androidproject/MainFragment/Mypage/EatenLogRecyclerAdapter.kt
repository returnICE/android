package com.capstone.androidproject.MainFragment.Mypage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.EatenLogData
import kotlinx.android.synthetic.main.item_view_eatenlog.view.*

class EatenLogRecyclerAdapter(private val items: ArrayList<EatenLogData>) :
        RecyclerView.Adapter<EatenLogRecyclerAdapter.ViewHolder>(){

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: EatenLogData) {

            var year = item.eatenDate.slice(IntRange(0,3))
            var month = item.eatenDate.slice(IntRange(5,6))
            var day = item.eatenDate.slice(IntRange(8,9))
            var hour = item.eatenDate.slice(IntRange(11,12))
            var minute = item.eatenDate.slice(IntRange(14,15))
            view.ELdate.setText("승인 날짜 : " + year + "년 " + month + "월 " +day + "일 " +hour + "시 " +minute + "분")
            view.ELmenuName.setText(item.menuName)
            view.ELprice.setText("가격 : " + item.price.toString() + " 원")

            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_eatenlog, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
     }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked: ${item.eatenId}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }
}