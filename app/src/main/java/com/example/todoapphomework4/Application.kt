package com.example.todoapphomework4

import android.app.Application
import com.example.todoapphomework4.service.ServiceConnector

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        ServiceConnector.init()
    }
}