package com.example.app_copains_ecole.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

class UserBean(
    var id: Long? = null,
    var pseudo: String? = null,
    var password: String? = null,
    var longitude: Double? = null,
    var latitude: Double? = null,
    var group_users: Int? = null,
    var session: String? = null
): Serializable
{
    fun toLatLng() = if(latitude!=null && longitude!=null) LatLng(latitude!!, longitude!!) else null
}

//variante
data class UserBeanList(val list: List<UserBean>)