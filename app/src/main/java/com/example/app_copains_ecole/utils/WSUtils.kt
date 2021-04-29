package com.example.app_copains_ecole.utils

import com.example.app_copains_ecole.model.UserBean
import com.google.gson.Gson

val gson = Gson()
const val URL = "http://192.168.0.19:8080"

class WsUtils() {
    companion object {

        fun register(userBean: UserBean): Long? {
            val json = gson.toJson(userBean)
            val request = sendPostOkHttpRequest("$URL/register", json)
            val userId = gson.fromJson(request, UserBean::class.java)
            return userId.id
        }

        fun login(userBean: UserBean): Long? {
            val json = gson.toJson(userBean)
            val request = sendPostOkHttpRequest("$URL/login", json)
            val userId = gson.fromJson(request, UserBean::class.java)
            return userId.id
        }

        fun setUserCoord(userBean: UserBean) {
            val json = gson.toJson(userBean)
            val request = sendPostOkHttpRequest("$URL/setUserCoord", json)
        }

        fun getUsers(): List<UserBean> {
            val request = sendGetOkHttpRequest("$URL/getUsers")
            val user = gson.fromJson(request, UserBean::class.java)
            return listOf(user)
        }
    }
}

