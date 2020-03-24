package com.capstone.androidproject

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushAlertService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            val messageBody:String = remoteMessage.notification!!.body.toString()
            val messageTitle:String = remoteMessage.notification!!.title.toString()

            val intent: Intent = Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent: PendingIntent =
                PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

            val channelId: String = "Channel ID"
            val defaultSoundUri: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.drawable.ic_add_shopping_cart_black_24dp)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
            val notificationManager: NotificationManager =
                getSystemService (Context.NOTIFICATION_SERVICE) as NotificationManager

            val channelName: String = "channel Name"
            val channel:NotificationChannel = NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)

            notificationManager.notify(0,notificationBuilder.build())
        }
    }

//    firebase에서 받아온 토큰을 서버에 저장
//    이 토큰으로 기기 구분
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("test1","tokenn : "+token)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token)
    }

}
