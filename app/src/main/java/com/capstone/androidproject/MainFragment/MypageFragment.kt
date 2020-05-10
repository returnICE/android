package com.capstone.androidproject.MainFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.LoginActivity
import com.capstone.androidproject.MainFragment.Mypage.MypageRecyclerAdapter
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.Success
import com.capstone.androidproject.Response.UserInfoResponse
import com.capstone.androidproject.ServerConfig.ServerConnect
import com.capstone.androidproject.SharedPreferenceConfig.App
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.capstone.androidproject.Response.SubedItemData as SubedItemData

class MypageFragment() : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var subeds: ArrayList<SubedItemData> = ArrayList()
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

        //getname(context!!.applicationContext)

        val _subeds = arguments?.getSerializable("subeds")!! as ArrayList<SubedItemData>
        subeds = _subeds
        Log.d("test", "subeds Mypage" + subeds)
        setContent(v, subeds)
        Log.d("test", "subeds Mypage2" + subeds)

        return v
    }

    fun setActionBar(){// 액션 바 설정
        activity!!.titleText.setText("마이페이지")
        activity!!.locationIcon.visibility = View.GONE
        activity!!.titleText.isClickable = false
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

    fun getname(ctx: Context){
        val serverConnect = ServerConnect(ctx)
        val server = serverConnect.conn()

        Log.d("test", "token Mypage : " + App.prefs.data)
        server.getCustomerInfoRequest(App.prefs.data).enqueue(object : Callback<UserInfoResponse>{
            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Toast.makeText(ctx, "사용자 이름 받아오기 실패", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                val succ = response.body()?.success
                val userinfo = response.body()?.data

                if (succ == false) {
                    Toast.makeText(ctx, "사용자 이름 받아오기 실패 2", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(ctx, "사용자 이름 받아오기 성공", Toast.LENGTH_SHORT).show()
                    Log.d("test", "name Mypage : " + userinfo?.name)
                    textMypageName.setText(userinfo?.name + "님")

                }
            }

        })

    }

    fun setContent(v:View, subeds:ArrayList<SubedItemData>) {

        val adapter = MypageRecyclerAdapter(subeds)
        val rv: RecyclerView = v.findViewById(R.id.recyclerViewMypage)
        Toast.makeText(context, "리사이클러뷰 테스트", Toast.LENGTH_SHORT).show()
        rv.adapter = adapter
        rv.addItemDecoration(
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.HORIZONTAL)
        )
        val lm = LinearLayoutManager(context, HORIZONTAL, false)
        rv.layoutManager = lm
    }
}
