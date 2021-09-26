package com.example.todoapphomework4.modelsResponses

import com.google.gson.annotations.SerializedName

class User {
    @SerializedName("name")
    var userName: String? = null
    var age: Int? = null
    var email: String? = null
    var token: String? = null

    companion object {
        var user: User? = null

        fun getCurrentInstance(): User {
            if (user == null) user = User()
            return user!!
        }
    }

    fun setUser(registeredUser: User) {
        user?.age = registeredUser.age
        user?.userName = registeredUser.userName
        user?.email = registeredUser.email
    }
}