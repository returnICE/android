package com.capstone.androidproject.HomeTab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.ItemData
import kotlinx.android.synthetic.main.item_view.view.*

class ItemRecyclerAdapter(private val items: ArrayList<ItemData>) :
    RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder>() {

    override fun getItemCount() = items.size

    /*  onBindViewHolder
    *   View가 생성되면 호출
    *   생성된 View에 보여줄 데이터를 설정
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener {it ->
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
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)

        return ViewHolder(inflatedView)
    }

    /*  Class ViewHolder
    *   ViewHolder 단위 객체로 View의 데이터를 설정
     */
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(listener: View.OnClickListener, item: ItemData) {
            view.imgItem.setImageResource(R.drawable.example_img)
            view.itemName.setText(item.type)
            view.originPrice.setText(item.originprice)
            view.salePrice.setText(item.saleprice)

            view.setOnClickListener(listener)
        }
    }
}