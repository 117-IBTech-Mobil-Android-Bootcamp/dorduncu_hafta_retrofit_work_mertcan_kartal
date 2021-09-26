package com.example.todoapphomework4.service

interface BaseResponseHandlerInterface<T> {
    fun onSuccess(data: T)
    fun onFailure()
}