package com.capstone.androidproject.MainFragment.Mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import kotlinx.android.synthetic.main.item_view_eatenlog.view.*

class EatenLogRecyclerAdapter() :
        RecyclerView.Adapter<EatenLogRecyclerAdapter.ViewHolder>(){

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(listener: View.OnClickListener) {


            view.ELdate.setText("2020-05-18 17:00")
            view.ELmenuName.setText("아메리카노")
            view.ELprice.setText("5000원")
            view.ELscore.setText("4점")

            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_eatenlog, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        //return items.size
        return 3
     }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val item = items[position]
        val listener = View.OnClickListener { it ->

            //Toast.makeText(it.context, "Clicked: ${item.name}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener)
            //itemView.tag = item
        }



    }

}