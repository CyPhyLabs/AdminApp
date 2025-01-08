package com.example.admint2t
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.admint2t.Login.Companion.getBearerToken
import com.example.admint2t.APISetup.APIService
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        // Send the token to your server or store it locally

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "From: ${remoteMessage.from}")

        // Check if the message contains a notification payload
        remoteMessage.notification?.let {
            Log.d("FCM", "Message Notification Title: ${it.title}")
            Log.d("FCM", "Message Notification Body: ${it.body}")

            val messageId = remoteMessage.data["message_id"] ?: "unknown"
            Log.d("FCM", "Message Notification Data: $messageId")

            // Show notification
            showNotification(it.title ?: "New Message", it.body ?: "You have a new message.", messageId)

            // Call API to mark the message as delivered
            if (messageId != "unknown") {
                someApiCall(messageId)
            }
        }
    }

    private fun showNotification(title: String, message: String, messageId: String?) {
        // Check if the permission is granted (required on Android 13 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted, handle the situation
                Log.w("FCM", "Notification permission not granted")
                return
            }
        }

        // Create an intent that opens the Inbox activity when the notification is tapped
        val intent = Intent(this, Inbox::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("message_id", messageId)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create a notification channel (required for Android 8.0 and above)
        val channelId = "default_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Default Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel for default notifications"
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your app's icon
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) // Dismiss the notification after it's tapped

        // Show the notification
        with(NotificationManagerCompat.from(this)) {
            try {
                notify(0, notificationBuilder.build())
            } catch (e: SecurityException) {
                Log.w("FCM", "SecurityException: Unable to show notification", e)
            }
        }
    }

    private fun someApiCall(messageId: String) {
        val token = getBearerToken(this)
        if (token != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val apiService = APIService()
                apiService.markMessageAsDelivered(messageId, token)
            }
        } else {
            Log.e("FCM", "No token found for API call")
        }
    }
}
