package com.capstone.androidproject.StoreInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import kotlinx.android.synthetic.main.item_view_menu.view.*
import kotlinx.android.synthetic.main.item_view_menu.view.price
import kotlinx.android.synthetic.main.item_view_subs.view.*




class SubsListRecyclerAdapter(private var items: MutableList<Item>) :
    RecyclerView.Adapter<SubsListRecyclerAdapter.ViewHolder>() {

    companion object {
        val HEADER = 0
        val CHILD = 1
    }

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = items.get(position).type

    /*  onBindViewHolder
    *   View가 생성되면 호출
    *   생성된 View에 보여줄 데이터를 설정
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked: ${item.type}", Toast.LENGTH_SHORT).show()
        }

        when(item.type){
            HEADER ->{
                holder.apply {
                    headerBind(listener, item)
                    itemView.tag = item
                }
            }
            CHILD ->{
                holder.apply {
                    childBind(listener, item)
                    itemView.tag = item
                }
            }
        }

    }

    /*  onCreateViewHolder
    *   RecyclerView가 초기화 될 때 호출
    *   보여줄 아이템 개수만큼 View를 생성
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val headerInflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_view_subs, parent, false)
        val childInflatedView = LayoutInflater.from(parent.context).inflate(R.layout.item_view_menu, parent, false)

        when(viewType){
            HEADER -> {
                return ViewHolder(headerInflatedView)
            }
            CHILD ->{
                return ViewHolder(childInflatedView)
            }
        }
        return ViewHolder(headerInflatedView)
    }

    /*  Class ViewHolder
    *   ViewHolder 단위 객체로 View의 데이터를 설정
     */
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        lateinit var referalItem: Item

        fun headerBind(listener: View.OnClickListener, item: Item) {
            referalItem = item

            if (item.e is StoreActivity.SubInfo) {
                view.subsName.setText(item.e.subName)
                view.price.setText(item.e.price.toString() + "원")
            }

            if (item.invisibleChildren == null) {
                view.btnExpandToggle.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
            } else {
                view.btnExpandToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
            }
            view.btnExpandToggle.setOnClickListener {
                if (item.invisibleChildren == null) {
                    item.invisibleChildren = ArrayList()
                    var count = 0
                    val pos = items.indexOf(referalItem)
                    while (items.size > pos + 1 && items.get(pos + 1).type == CHILD) {
                        val i = items.removeAt(pos + 1)
                        item.invisibleChildren?.add(i)
                        count++
                    }
                    notifyItemRangeRemoved(pos + 1, count)
                    view.btnExpandToggle.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp)
                } else {
                    val pos = items.indexOf(referalItem)
                    var index = pos + 1
                    for (i in item.invisibleChildren!!) {
                        items.add(index, i)
                        index++
                    }
                    notifyItemRangeInserted(pos + 1, index - pos - 1)
                    view.btnExpandToggle.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp)
                    item.invisibleChildren = null
                }
            }
            view.setOnClickListener(listener)
        }
        fun childBind(listener: View.OnClickListener, item: Item) {
            if(item.e is StoreActivity.MenuInfo) {
                view.menuName.setText(item.e.menuName)
                view.price.setText(item.e.price.toString() + "원")
            }

            view.setOnClickListener(listener)
        }
    } //https://dreamaz.tistory.com/223

    class Item(val type:Int,val e:Any){
        var invisibleChildren:MutableList<Item>? = null
    }
}