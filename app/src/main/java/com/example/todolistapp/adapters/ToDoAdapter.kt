package com.example.todolistapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistapp.R
import com.example.todolistapp.activites.AddToDoActivity
import com.example.todolistapp.activites.MainActivity
import com.example.todolistapp.database.DatabaseHandler
import com.example.todolistapp.models.ToDoModel
import kotlinx.android.synthetic.main.item_todo.view.*


open class ToDoAdapter(
    private val context: Context,
    private var list: ArrayList<ToDoModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.tvTitle.text = model.task
            holder.itemView.cbDone.isChecked = model.isFinished != 0

            holder.itemView.setOnClickListener {

                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }

            holder.itemView.cbDone.setOnCheckedChangeListener { _, isChecked ->
                model.isFinished = if (isChecked) 1 else 0
                val dbHandler = DatabaseHandler(context)
                dbHandler.updateToDo(model)

            }


        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int){
        val intent = Intent(context, AddToDoActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_Todo_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

    fun removeAt(position: Int){
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deleteToDo(list[position])
        if(isDeleted > 0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ToDoModel)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}