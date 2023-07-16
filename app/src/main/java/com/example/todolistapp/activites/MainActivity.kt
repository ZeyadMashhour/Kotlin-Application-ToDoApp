package com.example.todolistapp.activites

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R
import com.example.todolistapp.adapters.ToDoAdapter
import com.example.todolistapp.database.DatabaseHandler
import com.example.todolistapp.models.ToDoModel
import kotlinx.android.synthetic.main.activity_main.*
import pl.kitek.rvswipetodelete.SwipeToDeleteCallback
import pl.kitek.rvswipetodelete.SwipeToEditCallback


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddToDo.setOnClickListener {
            intent = Intent(this@MainActivity, AddToDoActivity::class.java)
            startActivityForResult(intent, ADD_Todo_REQUEST_CODE)
        }
        getToDoListFromLocalDatabase()
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_Todo_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK)
                getToDoListFromLocalDatabase()
        }else{
            Log.e("Activity" , "Cancelled or back pressed")
        }
    }

    private fun getToDoListFromLocalDatabase() {

        val dbHandler = DatabaseHandler(this)
        val getToDoList: ArrayList<ToDoModel> = dbHandler.getTodoList()

        if(getToDoList.size > 0){
            rv_todo_list.visibility = View.VISIBLE
            tv_no_records_available.visibility = View.GONE
            setUpToDoRecyclerView(getToDoList)
        }else{
            rv_todo_list.visibility = View.GONE
            tv_no_records_available.visibility = View.VISIBLE
        }

    }

    private fun setUpToDoRecyclerView(toDoList: ArrayList<ToDoModel>) {
        rv_todo_list.layoutManager = LinearLayoutManager(this)
        rv_todo_list.setHasFixedSize(true)

        val placesAdapter = ToDoAdapter(this, toDoList)
        rv_todo_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object : ToDoAdapter.OnClickListener{
            override fun onClick(position: Int, model: ToDoModel) {
                val intent = Intent(this@MainActivity, ToDoDetailsActivity::class.java)
                intent.putExtra(EXTRA_Todo_DETAILS, model)
                startActivity(intent)
            }
        })
        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = rv_todo_list.adapter as ToDoAdapter
                adapter.notifyEditItem(
                    this@MainActivity,
                    viewHolder.adapterPosition,
                    ADD_Todo_REQUEST_CODE
                )

            }
        }
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_todo_list)

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = rv_todo_list.adapter as ToDoAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                getToDoListFromLocalDatabase()
            }
        }
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_todo_list)
    }


    companion object{
        var ADD_Todo_REQUEST_CODE = 1
        var EXTRA_Todo_DETAILS = "extra_Todo_details"
    }

}