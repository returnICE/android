package com.capstone.androidproject.MainFragment.Home

import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.SubedItemData
import com.capstone.androidproject.ServiceInfo.ServiceActivity
import kotlinx.android.synthetic.main.item_view_home.view.*
import kotlinx.android.synthetic.main.item_view_mypage_subeditem.view.*
import org.jetbrains.anko.startActivity

class HomeRecyclerAdapter(private val items: ArrayList<SubedItemData>) :
        RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>(){

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(listener: View.OnClickListener, item: SubedItemData) {


            view.imgHomePhoto.setImageResource(R.drawable.example_img)
            view.imgHomePhoto.setColorFilter(R.color.imgTint,PorterDuff.Mode.DARKEN)

            view.textHomeSellerName.setText(item.name)
            view.textHomeServices.setText(item.subName)
            view.textHomeEndTime.setText("유효기한  ~" + item.endDate.substring(5))
            var restTimes = item.limitTimes - item.usedTimes
            view.textHomeUsedTimes.setText(restTimes.toString())
            if(restTimes <= item.limitTimes/2){
                view.textHomeUsedTimes.setTextColor(Color.RED)
            }
            else{
                view.textHomeUsedTimes.setTextColor(Color.GREEN)
            }
            view.textHomeLimitTimes.setText(item.limitTimes.toString())

            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_home, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
     }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            it.context.startActivity<ServiceActivity>(
                "subedId" to item.subedId,
                "name" to item.name
            )
            Toast.makeText(it.context, "Clicked: ${item.name}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }



    }

}