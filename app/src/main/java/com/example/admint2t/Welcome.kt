package com.example.admint2t

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging

class Welcome : AppCompatActivity() {

    private val NOTIFICATION_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Check for notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Request notification permission
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), NOTIFICATION_PERMISSION_CODE)
            } else {
                // Permission already granted
                getDeviceTokenAndSubscribe()
            }
        } else {
            // No need to request permission on older versions
            getDeviceTokenAndSubscribe()
        }

        // Set up button listeners
        val signInButton: Button = findViewById(R.id.signInButton)
        val createAccountButton: Button = findViewById(R.id.createAccountButton)

        signInButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        createAccountButton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getDeviceTokenAndSubscribe()
                } else {
                    // Permission denied
                    Log.w("NotificationPermission", "Notification permission not granted")
                }
            }
        }
    }

    private fun getDeviceTokenAndSubscribe() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("FCM", "Device token: $token")

            // Subscribe to topics
            subscribeToTopics()
        }
    }

    private fun subscribeToTopics() {
        // Subscribe to 'Staff' topic
        FirebaseMessaging.getInstance().subscribeToTopic("staff")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to staff topic")
                } else {
                    Log.w("FCM", "Failed to subscribe to staff topic", task.exception)
                }
            }

        // Subscribe to 'everyone' topic
        FirebaseMessaging.getInstance().subscribeToTopic("everyone")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subscribed to everyone topic")
                } else {
                    Log.w("FCM", "Failed to subscribe to everyone topic", task.exception)
                }
            }
    }
}
