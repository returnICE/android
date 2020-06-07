package com.capstone.androidproject.MainFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.capstone.androidproject.LoginActivity
import com.capstone.androidproject.MainActivity
import com.capstone.androidproject.MainFragment.Mypage.*
import com.capstone.androidproject.R
import com.capstone.androidproject.Response.Success
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
        val v: View = inflater.inflate(R.layout.fragment_mypage, container, false)


        val _subeds = arguments?.getSerializable("subeds")!! as ArrayList<SubedItemData>
        subeds = _subeds

        /*
        val bundle = Bundle()
        bundle.putSerializable("subeds", subeds)

         */

        setContent(v, subeds)

        setActionBar()
        val customername = v.findViewById(R.id.textMypageName) as TextView
        customername.setText(App.prefs.name + "님")

        //로그아웃
        val btnlogout: ImageView = v.findViewById(R.id.btnLogout)
        btnlogout.setOnClickListener {
            App.prefs.clear()
            logout(context!!.applicationContext)
        }

        //승인로그 페이지
        val btnCheckEatenlog: ImageView = v.findViewById(R.id.btnCheckEatenlog)
        btnCheckEatenlog.setOnClickListener {
            activity?.startActivity<EatenLogActivity>()
        }

        //개인정보 수정 페이지
        val btnModificationInfo: ImageView = v.findViewById(R.id.btnModificationInfo)
        btnModificationInfo.setOnClickListener {
            activity?.startActivity<ModificationInfoActivity>()
        }

        //개인정보 수정 페이지
        val btnEnterpriseCode: ImageView = v.findViewById(R.id.btnEnterpriseCode)
        btnEnterpriseCode.setOnClickListener {
            activity?.startActivity<EnterpriseCodeActivity>()
        }

        //구독 해지
        val btnDelete: ImageView = v.findViewById(R.id.btnDelete)
        btnDelete.setOnClickListener {
            if(checkDeletable(subeds) == 1) {
                val intent = Intent(context, DeleteSubActivity::class.java)
                intent.putExtra("test", "test")
                intent.putExtra("subeds", subeds)
                activity?.startActivity(intent)
            }
            else{
                Toast.makeText(context, "해지할 구독 서비스가 없습니다", Toast.LENGTH_SHORT).show()
            }
        }


        return v
    }

    fun setActionBar() {// 액션 바 설정
        activity!!.titleText.setText("마이페이지")
        activity!!.titleText.isClickable = false
    }

    fun logout(ctx: Context) {
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


    fun setContent(v: View, subeds: ArrayList<SubedItemData>) {

        val adapter = MypageRecyclerAdapter(subeds)
        val rv: RecyclerView = v.findViewById(R.id.recyclerViewMypage)
        rv.adapter = adapter
        rv.addItemDecoration(
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.HORIZONTAL)
        )
        val lm = LinearLayoutManager(context, HORIZONTAL, false)
        rv.layoutManager = lm
    }

    fun checkDeletable(subeds: ArrayList<SubedItemData>) : Int {
        for (subed in subeds){
            if (subed.autoPay == 1){
                return 1
            }
        }
        return 0
    }
}
