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

            Log.d("testing","price in EatenLogRecyclerAdapter : " + item.price.toString())
            var score = item.score.toString()
            view.ELdate.setText("승인 날짜 : " + item.eatenDate)
            view.ELmenuName.setText(item.menuName)
            view.ELprice.setText("가격 : " + item.price.toString())
            if(score  != "null"){
                view.ELscore.setText("평점 : " + score)
            }

            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_eatenlog, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d("testing","items.size in EatenLogRecyclerAdapter : " + items.size.toString())
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