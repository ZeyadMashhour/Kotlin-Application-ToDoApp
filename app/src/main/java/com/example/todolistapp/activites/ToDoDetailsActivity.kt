package com.example.todolistapp.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolistapp.R
import com.example.todolistapp.models.ToDoModel
import kotlinx.android.synthetic.main.activity_to_do_details.*

class ToDoDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_details)

        var toDoDetailsModel: ToDoModel? = null

        if(intent.hasExtra(MainActivity.EXTRA_Todo_DETAILS)){
            toDoDetailsModel =
                intent.getSerializableExtra(
                    MainActivity.EXTRA_Todo_DETAILS) as ToDoModel

            tv_description.text = toDoDetailsModel.description
        }

        }
    }