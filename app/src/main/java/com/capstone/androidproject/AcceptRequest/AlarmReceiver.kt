package com.capstone.androidproject.AcceptRequest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val menuName = intent.getStringExtra("menuName")
        val serviceName = intent.getStringExtra("serviceName")
        val sellerName = intent.getStringExtra("sellerName")
        val currentTime = intent.getStringExtra("currentTime")
        val eatenId = intent.getIntExtra("eatenId",0)
        val notificationUtills = NotificationUtils(context)
        notificationUtills.notification(menuName, serviceName, sellerName, currentTime, eatenId)
        notificationUtills.createNotificationChannel()
    }

}