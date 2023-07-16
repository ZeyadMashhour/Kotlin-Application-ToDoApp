package com.example.todolistapp.database

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.example.todolistapp.models.ToDoModel


class DatabaseHandler(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASEVERSION){


    companion object{
        private const val DATABASEVERSION = 1
        private const val DATABASE_NAME = "ToDoDatabase"
        private const val TABLE_TODO = "ToDoTable"

        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_IS_FINISHED = "isFinished"
        private const val KEY_COUNTDOWN_TIME = "countdownTime"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_TODO_TABLE = ("CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_IS_FINISHED + " INTEGER,"
                + KEY_COUNTDOWN_TIME + " INTEGER)")
        db?.execSQL(CREATE_TODO_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TODO")
        onCreate(db)
    }

    fun addToDo(toDo: ToDoModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, toDo.task)
        contentValues.put(KEY_DESCRIPTION, toDo.description)
        contentValues.put(KEY_COUNTDOWN_TIME, toDo.countdownTime)

        val result = db.insert(TABLE_TODO, null, contentValues)

        db.close()
        return result
    }

    fun updateToDo(toDo: ToDoModel): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, toDo.task)
        contentValues.put(KEY_DESCRIPTION, toDo.description)
        contentValues.put(KEY_IS_FINISHED, toDo.isFinished)
        contentValues.put(KEY_COUNTDOWN_TIME, toDo.countdownTime)

        val success = db.update(
            TABLE_TODO,
            contentValues,
            KEY_ID + "=" + toDo.id, null)

        db.close()
        return success
    }


    @SuppressLint("Range")
    fun getTodoList():ArrayList<ToDoModel>{
        val TodoList = ArrayList<ToDoModel>()
        val selectQuery = " Select * FROM $TABLE_TODO"
        val db = this.readableDatabase

        try{
            val cursor: Cursor = db.rawQuery(selectQuery, null)

            if(cursor.moveToFirst()){
                do{

                    val place = ToDoModel(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndex(KEY_IS_FINISHED)),
                        cursor.getInt(cursor.getColumnIndex(KEY_COUNTDOWN_TIME)))

                    TodoList.add(place)

                }while(cursor.moveToNext())
            }
            cursor.close()
        }catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        return TodoList
    }

    fun deleteToDo(toDo: ToDoModel): Int{
        val db = this.writableDatabase
        val success = db.delete(
            TABLE_TODO,
            KEY_ID + "=" + toDo.id, null)
        db.close()
        return success
    }

}