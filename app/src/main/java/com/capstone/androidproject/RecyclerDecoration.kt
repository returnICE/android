package com.capstone.androidproject

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue



class RecyclerDecoration(var context: Context, var height:Int) : RecyclerView.ItemDecoration() {

    var divHeight:Int = 0
    init{
        this.divHeight = dpToPx(context, height)
    }

    override fun getItemOffsets(outRect : Rect, view : View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if(parent.getChildAdapterPosition(view) != parent.adapter!!.itemCount - 1) {
            outRect.bottom = divHeight
        }
    }
    private fun dpToPx(context: Context, dp: Int): Int {

        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.getResources().getDisplayMetrics()
        ).toInt()
    }
}