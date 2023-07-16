package com.example.todolistapp.models

import java.io.Serializable

data class ToDoModel(
    var id: Int,
    var task: String,
    var description: String,
    var isFinished: Int = 0,
    var countdownTime: Int
): Serializable