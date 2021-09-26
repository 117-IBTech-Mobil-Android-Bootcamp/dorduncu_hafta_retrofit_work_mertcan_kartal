package com.example.todoapphomework4.modelsResponses

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("user") val user: User,
    val token: String
)