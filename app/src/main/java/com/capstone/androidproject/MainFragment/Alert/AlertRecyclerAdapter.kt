package com.capstone.androidproject.MainFragment.Alert

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.CampaignData
import com.capstone.androidproject.StoreInfo.StoreActivity
import kotlinx.android.synthetic.main.item_view_alert.view.*
import org.jetbrains.anko.startActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter




class AlertRecyclerAdapter(private val items: ArrayList<CampaignData>) :
        RecyclerView.Adapter<AlertRecyclerAdapter.ViewHolder>(){

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bind(listener: View.OnClickListener, item: CampaignData) {

            view.alertSellerName.setText(item.campaign.seller.name)
            view.alertTitle.setText(item.campaign.title)
            val t = item.campaign.transmitDate.substring(0,22)
            val localDateTime = LocalDateTime.parse(t)
            val formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm")
            val output = formatter.format(localDateTime)
            view.alertDate.setText(output)

            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_alert, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
     }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener { it ->
            it.context.startActivity<StoreActivity>(
                "sellerId" to item.campaign.sellerId
            )
        }
        holder.apply {
            bind(listener, item)
            itemView.tag = item
        }



    }

}