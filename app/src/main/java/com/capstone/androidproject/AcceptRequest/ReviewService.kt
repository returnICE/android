package com.capstone.androidproject.AcceptRequest

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.capstone.androidproject.R

class ReviewService : Service() {
    private val binder = LocalBinder()

    inner class LocalBinder : Binder(){
        fun getService(): ReviewService = this@ReviewService
    }
    override fun onBind(intent: Intent?): IBinder? {
        Log.d("testing", "start onBInd on ReviewService")
        val menuName = "testmenu"
        val serviceName = "servicename"
        val sellerName = "test"
        val currentTime = "test"
        val eatenId = 22
        Handler().postDelayed(
            {
                notification(menuName, serviceName, sellerName, currentTime, eatenId)
                createNotificationChannel()

            },
            10000
        )
        Log.d("testing", "Handler finished")
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
    //알림
    fun notification(menuName:String, serviceName:String, sellerName:String, currentTime:String, eatenId: Int){
        val notificationId = R.integer.notification_id
        val CHANNEL_ID  = R.string.channel_id.toString()
        var title = "평점을 입력해주세요"
        var content = menuName+" 어떠셨나요?"

        //푸시에서 클릭시 전환할 생성할 intent
        val intent = Intent(this@ReviewService, ReviewActivity::class.java).apply{
            // flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            //putExtra("eatenId",eatenId)
            //putExtra("menuName",menuName)
            //putExtra("serviceName",serviceName)
            //putExtra("sellerName",sellerName)
            //putExtra("currentTime",currentTime)
        }

        val fullScreenPendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        //var bitmap = BitmapFactory.decodeResource(resources, R.drawable.phone)

        // 알림 생성
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            //.setLargeIcon(bitmap)
            //.setShowWhen(true)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            //.setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(fullScreenPendingIntent)
            .setFullScreenIntent(fullScreenPendingIntent, true)//headup notation
            .setDefaults(Notification.DEFAULT_ALL)
            .setTicker("Notification")
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))

        NotificationManagerCompat.from(this).notify(notificationId, builder.build())

    }

    //알림 채널 생성
    private fun createNotificationChannel(){
        val CHANNEL_ID = R.string.channel_id.toString()
        val CHANNEL_NAME = R.string.channel_name.toString()
        val CHANNEL_DESCRIPTION = R.string.channel_description.toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
                lightColor = Color.GREEN
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}