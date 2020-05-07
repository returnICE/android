package com.capstone.androidproject.MainFragment.Mypage

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.SellerData
import com.capstone.androidproject.Response.SubedItemData
import kotlinx.android.synthetic.main.activity_find_to_map.view.textSellerName
import kotlinx.android.synthetic.main.item_view_mypage_subeditem.view.*
import java.net.URL

//SellerData -> SubedItemData
class MypageRecyclerAdapter(private val items: ArrayList<SellerData>) :
        RecyclerView.Adapter<MypageRecyclerAdapter.ViewHolder>(){

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        //SellerData -> SubedItemData
        fun bind(listener: View.OnClickListener, item: SellerData) {


            view.imgMypagePhoto.setImageResource(R.drawable.example_img)
            view.imgMypagePhoto.setColorFilter(R.color.imgTint, PorterDuff.Mode.DARKEN)

            /*var image_mypage_subeditem: URL2Bitmap = URL2Bitmap()
            image_mypage_subeditem = URL2Bitmap().apply {
                url = URL(item.subId)//이미지 가져와야함
            }
            var bitmap: Bitmap = image_mypage_subeditem.execute().get()

            view.imgMypagePhoto.setImageBitmap(bitmap)

             */
            //view.textSellerName.setText(item.subId)
            //view.textSubedItmeName.setText(item.subedId)
            view.textSellerName.setText("test rest name")
            view.textSubedItmeName.setText("test service name")

            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_mypage_subeditem, parent, false)
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