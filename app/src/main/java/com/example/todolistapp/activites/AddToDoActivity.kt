package com.example.todolistapp.activites

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.todolistapp.R
import com.example.todolistapp.database.DatabaseHandler
import com.example.todolistapp.models.ToDoModel
import kotlinx.android.synthetic.main.activity_add_to_do.*

class AddToDoActivity : BaseActivity(), View.OnClickListener {

    private var mToDoDetails: ToDoModel? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do)


        if(intent.hasExtra(MainActivity.EXTRA_Todo_DETAILS)){
            mToDoDetails = intent.getSerializableExtra(
                MainActivity.EXTRA_Todo_DETAILS) as ToDoModel
        }

        if(mToDoDetails != null){
            supportActionBar?.title = "Edit todo"

            et_title.setText(mToDoDetails!!.task)
            et_description.setText(mToDoDetails!!.description)
            btn_save.text = "UPDATE"
        }
        btn_save.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_save -> {
                val hoursStr = et_hours.text.toString()
                val minutesStr = et_minutes.text.toString()
                val secondsStr = et_seconds.text.toString()
                val hours = if (hoursStr.isEmpty()) 0 else hoursStr.toInt()
                val minutes = if (minutesStr.isEmpty()) 0 else minutesStr.toInt()
                val seconds = if (secondsStr.isEmpty()) 0 else secondsStr.toInt()
                val totalSeconds = hours * 3600 + minutes * 60 + seconds
                when{
                    et_title.text.isNullOrEmpty() ->{
                        Toast.makeText(this@AddToDoActivity, "Please enter title", Toast.LENGTH_SHORT).show()
                    }
                    et_description.text.isNullOrEmpty() ->{
                        Toast.makeText(this@AddToDoActivity, "Please enter description", Toast.LENGTH_SHORT).show()
                    }
                    else ->{
                        val toDoModel = ToDoModel(
                            if(mToDoDetails == null) 0 else mToDoDetails!!.id,
                            et_title.text.toString(),
                            et_description.text.toString(),
                        0,
                            totalSeconds
                        )

                        // Here we initialize the database handler class.
                        val dbHandler = DatabaseHandler(this)

                        if (mToDoDetails == null) {
                            val addTodo = dbHandler.addToDo(toDoModel)

                            if (addTodo > 0) {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        } else {
                            val updateTodo = dbHandler.updateToDo(toDoModel)

                            if (updateTodo > 0) {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }


}