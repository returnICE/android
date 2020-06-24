package com.capstone.androidproject.MainFragment.Home

import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.B2BData
import com.capstone.androidproject.Response.SubedItemData
import com.capstone.androidproject.ServiceInfo.B2BActivity
import com.capstone.androidproject.ServiceInfo.ServiceActivity
import kotlinx.android.synthetic.main.item_view_home.view.*
import kotlinx.android.synthetic.main.item_view_mypage_subeditem.view.*
import org.jetbrains.anko.startActivity

class HomeRecyclerAdapter(private val items: MutableList<Item>) :
        RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder>(){

    companion object {
        val SUBED = 0
        val ENTERPRISE = 1
    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(listener: View.OnClickListener, item: SubedItemData) {
            if(item.imgURL != null) {
                Glide.with(view.context)
                    .load(item.imgURL)
                    .into(view.imgHomePhoto)
            }
            else{
                view.imgHomePhoto.setImageResource(R.drawable.default_img)
            }
            view.imgHomePhoto.setColorFilter(Color.parseColor("#717171"),PorterDuff.Mode.MULTIPLY)

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

        fun b2bBind(listener: View.OnClickListener, item: B2BData) {
            if(item.imgURL != null) {
                Glide.with(view.context)
                    .load(item.imgURL)
                    .into(view.imgHomePhoto)
            }
            else{
                view.imgHomePhoto.setImageResource(R.drawable.default_img)
            }
            view.imgHomePhoto.setColorFilter(Color.parseColor("#717171"),PorterDuff.Mode.MULTIPLY)

            view.textHomeSellerName.setText(item.name)
            view.slash.setText("")
            view.textB2B.setText("기업용")
            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_home, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = items.get(position).type

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        when(item.type){
            ENTERPRISE->{
                if(item.e is B2BData) {
                    val listener = View.OnClickListener { it ->
                        it.context.startActivity<B2BActivity>(
                            "name" to item.e.name,
                            "sellerId" to item.e.sellerId
                        )
                    }
                    holder.apply {
                        b2bBind(listener, item.e)
                        itemView.tag = item.e
                    }
                }
            }
            SUBED->{
                if(item.e is SubedItemData) {
                    val listener = View.OnClickListener { it ->
                        it.context.startActivity<ServiceActivity>(
                            "subedId" to item.e.subedId,
                            "name" to item.e.name
                        )
                    }
                    holder.apply {
                        bind(listener, item.e)
                        itemView.tag = item.e
                    }
                }
            }
        }
    }

    class Item(val type:Int,val e:Any){
    }
}