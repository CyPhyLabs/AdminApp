// com/example/admint2t/api/NotificationService.kt

package com.example.admint2t

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

// Data class for Notifications (adjust fields as per your actual data structure)
data class Notification(
    val user_id: String,
    val message_id: String,
    val status: String,
    val acknowledged: Boolean,
    val message: Message
)

data class Message(
    val title: String,
    val body: String,
    val priority: String
)

// Define the API interface
interface NotificationApi {

    // PATCH request for marking message as delivered
    @PATCH("api/notifications/{message_id}/delivered/")
    suspend fun markAsDelivered(
        @Path("message_id") messageId: String,
        @Header("Authorization") authHeader: String
    ): Response<Void>

    // GET request for fetching notifications
    @GET("api/notifications/")
    suspend fun getNotifications(
        @Header("Authorization") authHeader: String
    ): Response<List<Notification>>
}

object NotificationService {
    private const val BASE_URL = "http://10.0.2.2:8000"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val notificationApi: NotificationApi = retrofit.create(NotificationApi::class.java)

    // Function to mark message as delivered
    fun markMessageAsDelivered(messageId: String, token: String?) {
        if (token != null) {
            val authHeader = "Bearer $token"

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = notificationApi.markAsDelivered(messageId, authHeader)
                    if (response.isSuccessful) {
                        Log.d("API", "Message $messageId marked as delivered")
                    } else {
                        Log.e("API", "Failed to mark message $messageId as delivered: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("API", "Error during API call", e)
                }
            }
        } else {
            Log.e("API", "Bearer token is not available")
        }
    }

    // Function to fetch notifications
    fun fetchNotifications(token: String?) {
        if (token != null) {
            val authHeader = "Bearer $token"

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = notificationApi.getNotifications(authHeader)
                    if (response.isSuccessful) {
                        val notifications = response.body()
                        if (notifications != null) {
                            Log.d("API", "Fetched ${notifications.size} notifications")
                            notifications.forEach { notification ->
                                Log.d("Notification", notification.toString())
                            }
                        } else {
                            Log.e("API", "No notifications found")
                        }
                    } else {
                        Log.e("API", "Failed to fetch notifications: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("API", "Error during API call", e)
                }
            }
        } else {
            Log.e("API", "Bearer token is not available")
        }
    }
}
