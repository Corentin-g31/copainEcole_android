package com.example.app_copains_ecole.utils

import com.example.app_copains_ecole.model.UserBean
import com.google.gson.Gson

val gson = Gson()
const val  URL = "http://localhost:8080"

class WsUtils(){
    companion object {

        fun register(userBean: UserBean)  {
            val json = gson.toJson(userBean)
            val request = sendPostOkHttpRequest("$URL/register",json)
        }

        fun login(userBean: UserBean){
            val json = gson.toJson(userBean)
            val request = sendPostOkHttpRequest("$URL/login",json)
        }

    }
}
