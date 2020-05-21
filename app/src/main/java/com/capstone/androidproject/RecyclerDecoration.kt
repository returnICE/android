package com.capstone.androidproject

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerDecoration(var height:Int) : RecyclerView.ItemDecoration() {

    var divHeight:Int = height

    override fun getItemOffsets(outRect : Rect, view : View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.top = divHeight
    }
}