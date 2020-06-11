package com.capstone.androidproject.ServiceInfo

import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.keyIterator
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.B2BData
import com.capstone.androidproject.Response.MenuData
import com.capstone.androidproject.Response.SubedItemData
import com.capstone.androidproject.ServiceInfo.B2BActivity
import com.capstone.androidproject.ServiceInfo.ServiceActivity
import kotlinx.android.synthetic.main.item_view_home.view.*
import kotlinx.android.synthetic.main.item_view_menu_clickable.view.*
import kotlinx.android.synthetic.main.item_view_mypage_subeditem.view.*
import org.jetbrains.anko.startActivity

class B2BRecyclerAdapter(private val items: ArrayList<MenuData>) :
        RecyclerView.Adapter<B2BRecyclerAdapter.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(v: View, pos: Int)
    }

    private var mSelectedItems: SparseBooleanArray = SparseBooleanArray(0)

    private var mListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(listener: View.OnClickListener, item: MenuData) {

            view.menuNameClickable.setText(item.menuName)
            view.priceClickable.setText(item.price.toString())
            view.menuIdClickable.setText(item.menuId.toString())
            view.setOnClickListener { it ->
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    mListener?.onItemClick(it, pos)
                }
                toggleItemSelected(pos)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_menu_clickable, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked: ${item.menuName}", Toast.LENGTH_SHORT).show()
        }

        holder.apply {
            bind(listener, item)
            itemView.tag = item
            itemView.isSelected = isItemSelected(position)
        }
    }

    private fun isItemSelected(position:Int):Boolean {
        return mSelectedItems.get(position, false)
    }

    private fun toggleItemSelected(position:Int) {
        if (mSelectedItems.get(position, false)) {
            mSelectedItems.delete(position)
            clearSelectedItem()
            notifyItemChanged(position)
        } else {
            clearSelectedItem()
            mSelectedItems.put(position, true)
            notifyItemChanged(position)
        }
    }
    fun clearSelectedItem() {
        var position = 0

        var i=0
        for (i in mSelectedItems.keyIterator()) {
            position = i
            mSelectedItems.put(position, false)
            notifyItemChanged(position)
        }

        mSelectedItems.clear()
    }


}