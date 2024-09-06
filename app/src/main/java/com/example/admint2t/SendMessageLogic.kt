package com.example.admint2t

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class SendMessageLogic {
    data class Message(
        val title: String,
        val target_audience: String,
        val body: String
    )

    interface ApiService {
        @POST("api/messages/create/")
        fun sendMessage(
            @Header("Authorization") authHeader: String, // Add the authorization header
            @Body message: Message
        ): Call<Void>
    }
}

