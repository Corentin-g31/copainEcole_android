package com.example.app_copains_ecole.utils

import com.example.app_copains_ecole.model.UserBean
import com.example.app_copains_ecole.model.UserBeanList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gson = Gson()
const val URL = "http://192.168.1.17:8080"

class WsUtils() {
    companion object {

        fun register(userBean: UserBean) {
            val json = gson.toJson(userBean)
            val request = sendPostOkHttpRequest("$URL/register", json)
            val userSession = gson.fromJson(request, UserBean::class.java)
            userBean.session = userSession.session
        }

        fun login(userBean: UserBean) {
            val json = gson.toJson(userBean)
            val request = sendPostOkHttpRequest("$URL/login", json)
            val userSession = gson.fromJson(request, UserBean::class.java)
            userBean.session = userSession.session
        }

        fun setUserCoord(userBean: UserBean) {
            val json = gson.toJson(userBean)
            val request = sendPostOkHttpRequest("$URL/setUserCoord", json)
        }

        fun getUsers(): List<UserBean> {
            val request = sendGetOkHttpRequest("$URL/getUsers")
            return gson.fromJson(request, object : TypeToken<ArrayList<UserBean>>() {}.type)
        }

        //variante
        //fun getUsers(): List<UserBean> {
        //    val request = sendGetOkHttpRequest("$URL/getUsers")
        //    val users = gson.fromJson(request, UserBeanList::class.java)
        //    return users.list
        //}
    }
}

