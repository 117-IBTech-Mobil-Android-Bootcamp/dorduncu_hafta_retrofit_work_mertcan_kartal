package com.example.todoapphomework4.modelsResponses

data class TaskResponse(
    val data: MutableList<Task>,
    val count: Int
)