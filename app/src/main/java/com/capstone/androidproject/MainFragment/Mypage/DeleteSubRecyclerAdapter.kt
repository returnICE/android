package com.capstone.androidproject.MainFragment.Mypage

import android.content.Context
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.util.keyIterator
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.EatenLogData
import com.capstone.androidproject.Response.SubedItemData
import kotlinx.android.synthetic.main.item_view_eatenlog.view.*
import kotlinx.android.synthetic.main.item_view_service.view.*


class DeleteSubRecyclerAdapter(private val items: ArrayList<SubedItemData>) :
    RecyclerView.Adapter<DeleteSubRecyclerAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(v: View, pos: Int)
    }

    private var mListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    private var mSelectedItems: SparseBooleanArray = SparseBooleanArray(0)

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(listener: View.OnClickListener, item: SubedItemData) {
            val context:Context
            Log.d("testing", "subedItem :: " + item)
            view.IVSsubName.setText(item.subName)
            view.IVSendDate.setText(item.endDate)
            view.IVSautoPay.setText(item.autoPay.toString())
            if(item.autoPay == 0){
                view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.textGray))
            }
            view.IVSsellerName.setText(item.name)
            view.IVSsubedId.setText(item.subedId.toString())
            view.setOnClickListener { it ->
                val posi = adapterPosition
                if (posi != RecyclerView.NO_POSITION) {
                    mListener?.onItemClick(it, posi)
                }
                toggleItemSelected(posi)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_service, parent, false)
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