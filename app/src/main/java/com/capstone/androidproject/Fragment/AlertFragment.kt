package com.capstone.androidproject.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.androidproject.R
import kotlinx.android.synthetic.main.activity_main.*

class AlertFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setActionBar()

        return inflater.inflate(R.layout.fragment_alert, container, false)
    }

    fun setActionBar(){// 액션 바 설정
        activity!!.titleText.setText("알람")
        activity!!.locationIcon.visibility = View.GONE
    }

}
