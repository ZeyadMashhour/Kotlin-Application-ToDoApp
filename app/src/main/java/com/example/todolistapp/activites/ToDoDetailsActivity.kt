package com.example.todolistapp.activites

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import com.example.todolistapp.R
import com.example.todolistapp.models.ToDoModel

import kotlinx.android.synthetic.main.activity_to_do_details.*

class ToDoDetailsActivity : BaseActivity() {
    private var remainingTimeMillis: Long = 0
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var toDoDetailsModel: ToDoModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_details)

        if (intent.hasExtra(MainActivity.EXTRA_Todo_DETAILS)) {
            toDoDetailsModel =
                intent.getSerializableExtra(
                    MainActivity.EXTRA_Todo_DETAILS
                ) as ToDoModel

            tv_description.text = toDoDetailsModel.description
            var countDownTime = secondsToTime(toDoDetailsModel.countdownTime)
            tv_hours.text = countDownTime.hours.toString()
            tv_minutes.text = ":" + countDownTime.minutes.toString()
            tv_seconds.text = ":" + countDownTime.seconds.toString()

            circularProgressbar.setOnClickListener{
                useCountdownTimer(toDoDetailsModel.countdownTime.toLong())
            }
        }

    }

    private fun useCountdownTimer(countDown: Long) {
        val millisInFuture = countDown * 1000 // convert to milliseconds

        countDownTimer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeMillis = millisUntilFinished

                val hours = millisUntilFinished / (1000 * 60 * 60)
                val minutes = (millisUntilFinished % (1000 * 60 * 60)) / (1000 * 60)
                val seconds = (millisUntilFinished % (1000 * 60)) / 1000

                tv_hours.text = String.format("%02d", hours)
                tv_minutes.text = String.format("%02d", minutes)
                tv_seconds.text = String.format("%02d", seconds)
            }

            override fun onFinish() {
                TODO("Not yet implemented")
            }
        }.start() // Start the timer
    }


    override fun onDestroy() {
        super.onDestroy()

        // Cancel the countdown timer to avoid memory leaks
        countDownTimer.cancel()
    }

}

