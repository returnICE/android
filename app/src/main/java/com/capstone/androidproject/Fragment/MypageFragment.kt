package com.capstone.androidproject.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.capstone.androidproject.LoginActivity
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.Success
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var v:View = inflater.inflate(R.layout.fragment_mypage, container, false)

        setActionBar()

        var btnlogout: ImageView = v.findViewById(R.id.btnLogout)
        btnlogout.setOnClickListener {
            App.prefs.clear()
            logout(context!!.applicationContext)
        }
        return v
    }

    fun setActionBar(){// 액션 바 설정
        activity!!.titleText.setText("마이페이지")
        activity!!.locationIcon.visibility = View.GONE
    }

    fun logout(ctx:Context) {
        val serverConnect = ServerConnect(ctx)
        val server = serverConnect.conn()

        server.getLogoutRequest().enqueue(object : Callback<Success> {
            override fun onFailure(call: Call<Success>?, t: Throwable?) {
                Toast.makeText(ctx, "로그아웃 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Success>?, response: Response<Success>?) {
                val succ = response?.body()

                if (succ?.success == false) {
                    Toast.makeText(ctx, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(ctx, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                    activity?.startActivity<LoginActivity>()
                    activity?.finish()
                }
            }
        })
    }
}
