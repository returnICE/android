package com.capstone.androidproject.AcceptRequest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.capstone.androidproject.MainActivity
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.EatenLogDataResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.ServiceInfo.ServiceActivity
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_accept.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class AcceptActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept)

        val menuName = intent.getStringExtra("menuName")
        val serviceName = intent.getStringExtra("serviceName")
        val sellerName = intent.getStringExtra("sellerName")
        val subedId = intent.getIntExtra("subedId",0)
        val menuId = Integer.parseInt(intent.getStringExtra("menuId"))
        val currentTime: LocalDateTime = LocalDateTime.now()

        ACtextMenuName.setText(menuName)
        ACtextServiceName.setText(serviceName)
        ACtextStoreName.setText(sellerName)

        ACbtnCertification.setOnClickListener(){
            accept(App.prefs.token, subedId, menuId, menuName, serviceName, sellerName, currentTime.toString())
        }

    }

    fun accept(token: String, subedId: Int, menuId: Int, menuName:String, serviceName:String, sellerName:String, currentTime:String) {
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.postAcceptRequest(token, subedId, menuId).enqueue(object:

            Callback<EatenLogDataResponse> {
            override fun onFailure(call: Call<EatenLogDataResponse>, t: Throwable) {
                Toast.makeText(this@AcceptActivity, "승인 실패1", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<EatenLogDataResponse>,
                response: Response<EatenLogDataResponse>
            ) {
                val success = response?.body()?.success
                val eatenLog = response?.body()?.data

                if (success == false) {
                    Toast.makeText(this@AcceptActivity, "승인 실패2", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@AcceptActivity, "승인 성공", Toast.LENGTH_SHORT).show()
                    val eatenId = eatenLog!!.eatenId
                    notification(menuName, serviceName, sellerName, currentTime, eatenId)
                    createNotificationChannel()
                    val nextIntent = Intent(this@AcceptActivity, MainActivity::class.java)
                    startActivity(nextIntent)
                    finishAffinity()
                }
            }
        })
    }

    //알림
    fun notification(menuName:String, serviceName:String, sellerName:String, currentTime:String, eatenId: Int){
        val notificationId = R.integer.notification_id
        val CHANNEL_ID  = R.string.channel_id.toString()
        var title = "평점을 입력해주세요"
        var content = menuName+" 어떠셨나요?"

        //푸시에서 클릭시 전환할 생성할 intent
        val intent = Intent(this@AcceptActivity, ReviewActivity::class.java).apply{
           // flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("eatenId",eatenId)
            putExtra("menuName",menuName)
            putExtra("serviceName",serviceName)
            putExtra("sellerName",sellerName)
            putExtra("currentTime",currentTime)
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