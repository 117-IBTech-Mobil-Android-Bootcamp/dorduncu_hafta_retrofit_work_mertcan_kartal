package com.example.todoapphomework4.activityFragmentUI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.example.todoapphomework4.R
import com.example.todoapphomework4.modelsResponses.User
import com.example.todoapphomework4.service.BaseCallBack
import com.example.todoapphomework4.service.ServiceConnector
import com.example.todoapphomework4.utils.USER_TOKEN
import com.example.todoapphomework4.utils.gone
import com.example.todoapphomework4.utils.toast
import com.google.android.material.progressindicator.LinearProgressIndicator

class SplashActivity : AppCompatActivity() {
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        if (isLoggedIn()) {
            User.getCurrentInstance().token = token

            ServiceConnector.restInterface.getMe().enqueue(object : BaseCallBack<User>() {
                override fun onSuccess(data: User) {
                    super.onSuccess(data)

                    progressBar.gone()
                    User.getCurrentInstance().setUser(data)

                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.putExtra("state", "home")
                    startActivity(intent)
                    finish()
                }

                override fun onFailure() {
                    super.onFailure()

                    toast("Please authenticate.")
                }
            })
        } else {
            progressBar.gone()

            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.putExtra("state", "login")
            startActivity(intent)
            finish()
        }
    }

    private fun isLoggedIn(): Boolean {
        val token = getToken()
        return token.isNotEmpty()
    }

    private fun getToken(): String {
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        token = sh.getString(USER_TOKEN, "")!!

        return token!!
    }
}