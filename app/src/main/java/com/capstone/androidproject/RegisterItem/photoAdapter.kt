package com.capstone.androidproject.RegisterItem

import android.content.Context
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R


class photoAdapter(val context: Context, val photoArray: ArrayList<photoList>):
RecyclerView.Adapter<photoAdapter.Holder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.photo_list, parent, false)
        return Holder(view)

    }
    override fun getItemCount(): Int {
        return photoArray.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int){
        holder.bind(photoArray[position], context)
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemPhoto = itemView.findViewById<ImageView>(R.id.imgPhoto)

        fun bind(photo : photoList, context: Context){
            if(photo.img != null){
                itemPhoto.setImageBitmap(photo.img)
            }
            else{
                //아무것도 없을 때
            }
        }

    }

}
