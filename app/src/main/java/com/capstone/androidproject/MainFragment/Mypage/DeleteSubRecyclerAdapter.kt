package com.capstone.androidproject.MainFragment.Mypage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.EatenLogData
import com.capstone.androidproject.Response.SubedItemData
import kotlinx.android.synthetic.main.item_view_eatenlog.view.*
import kotlinx.android.synthetic.main.item_view_service.view.*

class DeleteSubRecyclerAdapter(private val items: ArrayList<SubedItemData>) :
    RecyclerView.Adapter<DeleteSubRecyclerAdapter.ViewHolder>(){

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(listener: View.OnClickListener, item: SubedItemData) {
            Log.d("testing", "subedItem :: " + item)
            view.IVSsubName.setText(item.subName)
            view.IVSendDate.setText(item.endDate)
            view.IVSsellerName.setText(item.name)
            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_service, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked: ${item.name}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }



    }

}