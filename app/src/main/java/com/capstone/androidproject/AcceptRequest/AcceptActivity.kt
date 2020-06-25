package com.capstone.androidproject.AcceptRequest

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
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
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.system.measureTimeMillis

class AcceptActivity : AppCompatActivity() {

    private lateinit var mService : ReviewService
    private var mBound : Boolean = false

    private val connection = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("testing","service disconnected")
            mBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("testing","service connected")
            val binder = service as ReviewService.LocalBinder
            mService = binder.getService()
            mBound = true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept)

        val isB2B = intent.getIntExtra("isB2B", 0)
        val menuName = intent.getStringExtra("menuName")
        var serviceName = intent.getStringExtra("serviceName")
        val sellerName = intent.getStringExtra("sellerName")
        val price = intent.getIntExtra("price",0)
        val subedId = intent.getIntExtra("subedId",0)
        val menuId = Integer.parseInt(intent.getStringExtra("menuId"))
        val currentTime: LocalDateTime = LocalDateTime.now()

        if(isB2B == 0) {
            ACtextMenuName.setText(menuName)
            ACtextServiceName.setText(serviceName)
            ACtextStoreName.setText(sellerName)
        }
        else{
            ACtextMenuName.setText(price.toString() + "원")
            ACtextServiceName.setText(menuName)
            ACtextStoreName.setText(sellerName)
        }


        ACbtnCertification.setOnClickListener(){
            if(isB2B==0) {
                accept(
                    App.prefs.token,
                    subedId,
                    menuId,
                    menuName,
                    serviceName,
                    sellerName,
                    currentTime.toString()
                )
            }
            else{
                b2baccept(
                    App.prefs.token,
                    price,
                    menuId,
                    menuName,
                    sellerName,
                    currentTime.toString()
                )
            }
        }
    }

    /*
    override fun onStop() {
        super.onStop()
        //unbindService(connection)
        mBound = false
    }
    fun showTimerPickerFragment(view: View){
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, "time_picker")
    }

     */

    fun cancelAlarm(view: View){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.cancel(pendingIntent)

    }

    private fun startAlarm(eatenId : Int, menuName : String, serviceName : String, sellerName : String, currentTime : String){
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val cal = Calendar.getInstance()
        cal.time = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())
        cal.add(Calendar.SECOND, 10)
        intent.putExtra("eatenId",eatenId)
        intent.putExtra("menuName",menuName)
        intent.putExtra("serviceName",serviceName)
        intent.putExtra("sellerName",sellerName)
        intent.putExtra("currentTime",currentTime)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis,pendingIntent)

    }

    fun b2baccept(token: String, price: Int, menuId: Int, menuName: String, sellerName: String, currentTime: String){
        val serverConnect = ServerConnect(this)
        val server = serverConnect.conn()

        server.postEnterpriseAcceptRequest(token, price, menuId).enqueue(object:

            Callback<EatenLogDataResponse> {
            override fun onFailure(call: Call<EatenLogDataResponse>, t: Throwable) {
                Toast.makeText(this@AcceptActivity, "b2baccept 승인 실패1", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<EatenLogDataResponse>,
                response: Response<EatenLogDataResponse>
            ) {
                val success = response?.body()?.success
                val eatenLog = response?.body()?.data

                if (success == false) {
                    Toast.makeText(this@AcceptActivity, "b2baccept 승인 실패2", Toast.LENGTH_SHORT).show()
                } else {
                    val eatenId = eatenLog!!.eatenId
                    //notification(menuName, serviceName, sellerName, currentTime, eatenId)
                    //createNotificationChannel()
                    /*val nextService = Intent(this@AcceptActivity, ReviewService::class.java).also{ intent ->
                        startForegroundService(intent)
                        //bindService(intent, connection, Context.BIND_AUTO_CREATE)
                    }*/

                    startAlarm(eatenId, menuName, "", sellerName, currentTime)
                    val nextIntent = Intent(this@AcceptActivity, MainActivity::class.java)
                    nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(nextIntent)

                    //finishAffinity()
                }
            }
        })
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
                    val eatenId = eatenLog!!.eatenId
                    //notification(menuName, serviceName, sellerName, currentTime, eatenId)
                    //createNotificationChannel()
                    /*val nextService = Intent(this@AcceptActivity, ReviewService::class.java).also{ intent ->
                        startForegroundService(intent)
                        //bindService(intent, connection, Context.BIND_AUTO_CREATE)
                    }*/

                    startAlarm(eatenId, menuName, serviceName, sellerName, currentTime)
                    val nextIntent = Intent(this@AcceptActivity, MainActivity::class.java)
                    nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(nextIntent)

                    //finishAffinity()
                }
            }
        })
    }

}