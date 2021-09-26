package com.example.todoapphomework4.modelsResponses

import com.google.gson.annotations.SerializedName

class Task {

    var swipedOrNot: Boolean = false
    var viewType: Int = 1
    var completed: Boolean = false

    @SerializedName
        ("_id")
    var id: String? = null
    var description: String? = null
    var owner: String? = null
    var createdAt: String? = null
    var updatedAt: String? = null
}