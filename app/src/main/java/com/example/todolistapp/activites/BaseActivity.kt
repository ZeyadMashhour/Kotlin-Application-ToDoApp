package com.example.todolistapp.activites

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.todolistapp.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
    }

    data class Time(val hours: Int, val minutes: Int, val seconds: Int)

    fun secondsToTime(secondsInput: Int): Time {
        val hours = secondsInput / 3600
        val minutes = (secondsInput % 3600) / 60
        val seconds = secondsInput % 60

        return Time(hours, minutes, seconds)
    }
}