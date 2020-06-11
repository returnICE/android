package com.capstone.androidproject.AcceptRequest

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class TimePickerFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(
            activity,
            activity as TimePickerDialog.OnTimeSetListener,
            1,
            1,
            true
        )
    }

}