package com.example.admint2t.APISetup

import android.util.Log
import com.example.admint2t.AuthService
import com.example.admint2t.LoginRequest
import com.example.admint2t.LoginResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

import retrofit2.http.Body
import retrofit2.http.POST

// Data classes for Notifications and Messages
data class Notification(
    val user_id: String,
    val message_id: String,
    val status: String,
    val acknowledged: Boolean,
    val message: Message,
    val created_at: String,
    val updated_at: String
)

data class Message(
    val title: String,
    val body: String,
    val priority: String? = null, // Optional priority field
    val target_audience: String
)

// Request and response data classes


interface NotificationApi {
    @PATCH("api/notifications/{message_id}/delivered/")
    suspend fun markAsDelivered(
        @Path("message_id") messageId: String,
        @Header("Authorization") authHeader: String
    ): Response<Void>

    @GET("api/notifications/")
    suspend fun getNotifications(
        @Header("Authorization") authHeader: String
    ): Response<List<Notification>>
}

interface MessageApi {
    @POST("api/messages/create/")
    suspend fun sendMessage(
        @Header("Authorization") authHeader: String,
        @Body message: Message
    ): Response<Void>
}

// API Service Class
class APIService {
    private val BASE_URL = "http://10.0.2.2:8000"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authService: AuthService = retrofit.create(AuthService::class.java)
    private val notificationApi: NotificationApi = retrofit.create(NotificationApi::class.java)
    private val messageApi: MessageApi = retrofit.create(MessageApi::class.java)

    // Function to perform login
    suspend fun login(email: String, password: String): LoginResponse {
        return authService.login(LoginRequest(email, password))
    }


    // Function to mark message as delivered
    suspend fun markMessageAsDelivered(messageId: String, token: String) {
        try {
            val response = notificationApi.markAsDelivered(messageId, "Bearer $token")
            if (response.isSuccessful) {
                Log.d("API", "Message $messageId marked as delivered")
            } else {
                Log.e(
                    "API",
                    "Failed to mark message $messageId as delivered: ${
                        response.errorBody()?.string()
                    }"
                )
            }
        } catch (e: Exception) {
            Log.e("API", "Error during API call", e)
        }
    }

    // Function to fetch notifications
    suspend fun getNotifications(token: String): List<Notification>? {
        return try {
            val response = notificationApi.getNotifications("Bearer $token")
            if (response.isSuccessful) {
                response.body() // Return the list of notifications
            } else {
                Log.e("API", "Failed to fetch notifications: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("API", "Error during API call", e)
            null
        }
    }

    // Function to send a message
    suspend fun sendMessage(token: String, message: Message): Boolean {
        return try {
            val response = messageApi.sendMessage("Bearer $token", message)
            if (response.isSuccessful) {
                Log.d("API", "Message sent successfully")
                true // Message sent successfully
            } else {
                Log.e("API", "Failed to send message: ${response.errorBody()?.string()}")
                false // Message sending failed
            }
        } catch (e: Exception) {
            Log.e("API", "Error during API call", e)
            false // Message sending failed
        }
    }
}



