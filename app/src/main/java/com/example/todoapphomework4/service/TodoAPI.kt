package com.example.todoapphomework4.service

import com.example.todoapphomework4.modelsResponses.LoginResponse
import com.example.todoapphomework4.modelsResponses.TaskResponse
import com.example.todoapphomework4.modelsResponses.UpdateResponse
import com.example.todoapphomework4.modelsResponses.User
import retrofit2.Call
import retrofit2.http.*

interface TodoAPI {

    //get user
    @GET("user/me")
    fun getMe(): Call<User>

    //login
    @POST("user/login")
    fun login(@Body params: MutableMap<String, String>): Call<LoginResponse>

    //get all tasks
    @GET("task")
    fun getAllTasks(): Call<TaskResponse>

    //pagination
    @GET("task")
    fun getTaskByPagination(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): Call<TaskResponse>

    //update task
    @PUT("task/{id}")
    fun updateTask(
        @Path("id") id: String,
        @Body param: MutableMap<String, Boolean>
    ): Call<UpdateResponse>

    //delete task
    @DELETE("task/{id}")
    fun deleteTask(@Path("id") id: String): Call<UpdateResponse>

    //add task
    @POST("task")
    fun addTask(@Body param: MutableMap<String, String>): Call<UpdateResponse>
}