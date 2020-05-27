package com.capstone.androidproject.MainFragment.StoreList

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
import com.capstone.androidproject.Response.SellerData
import com.capstone.androidproject.StoreInfo.StoreActivity
import kotlinx.android.synthetic.main.item_view_seller.view.*
import org.jetbrains.anko.startActivity

class StoreListRecyclerAdapter(private val items: ArrayList<SellerData>) :
    RecyclerView.Adapter<StoreListRecyclerAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    /*  onBindViewHolder
    *   View가 생성되면 호출
    *   생성된 View에 보여줄 데이터를 설정
     */

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener {it ->
            it.context.startActivity<StoreActivity>(
                "sellerId" to item.sellerId,
                "name" to item.name
            )
            Toast.makeText(it.context, "Clicked: ${item.type}", Toast.LENGTH_SHORT).show()
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }
    }


    /*  onCreateViewHolder
    *   RecyclerView가 초기화 될 때 호출
    *   보여줄 아이템 개수만큼 View를 생성
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_view_seller, parent, false)

        return ViewHolder(
            inflatedView
        )
    }

    /*  Class ViewHolder
    *   ViewHolder 단위 객체로 View의 데이터를 설정
     */
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: SellerData) {

            if(item.imgURL != null) {
                Glide.with(view.context)
                    .load(item.imgURL)
                    .into(view.imgItem)
            }
            else{
                view.imgItem.setImageResource(R.drawable.default_img)
            }
            view.imgItem.setColorFilter(Color.parseColor("#717171"),PorterDuff.Mode.MULTIPLY)

            lateinit var distance:String
            if(item.distance < 10000) {
                val d = (item.distance/10).toInt()
                distance = d.toString() + "m"
            }
            else {
                val d = item.distance / 10000.0
                distance = String.format("%.1f", d) + "km"
            }
            view.distanceHome.setText(distance)
            view.subsCount.setText("구독자수 "+item.totalSubs.toString()+"명")
            view.storeName.setText(item.name)
            view.minPrice.setText(item.minPrice.toString()+"원")

            view.setOnClickListener(listener)
        }
    }
}