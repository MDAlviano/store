package com.alvian.esemkastore.repository

import com.alvian.esemkastore.http.HttpHandler
import org.json.JSONObject

object AuthRepository {

    fun login(email: String, password: String): String {
        val jsonBody = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        return HttpHandler.postRequest("api/Login", jsonBody)
    }

}