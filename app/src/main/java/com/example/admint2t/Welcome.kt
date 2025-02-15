package com.example.admint2t

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.admint2t.APISetup.APISubscription
import com.example.admint2t.APISetup.DeviceToken
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import RetrofitClient as RetrofitClient

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
                getSubscription()
            }
        } else {
            // No need to request permission on older versions
            getSubscription()
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
                    getSubscription()

                } else {
                    // Permission denied
                    Log.w("NotificationPermission", "Notification permission not granted")
                }
            }
        }
    }



    private fun getSubscription() {
        val bearerToken = Login.getBearerToken(this)
        val authHeader = "Bearer $bearerToken" // Ensure Bearer prefix
        Log.d("FCM", "Authorization header: $authHeader")

        if (bearerToken.isNullOrEmpty()) {
            Log.w("FCM", "Authorization header is null or empty")
            return
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val deviceToken = task.result
            if (deviceToken.isNullOrEmpty()) {
                Log.w("FCM", "Device token is null or empty")
                return@addOnCompleteListener
            }

            Log.d("FCM", "Device token: $deviceToken")
            val requestBody = DeviceToken(device_token = deviceToken)


            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val apiService = RetrofitClient.createService(APISubscription::class.java)
                    val response = apiService.subscribe(authHeader, requestBody)

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Log.d("FCM", "Subscribed to topics and registered device token")
                            Log.d("FCM", "Response headers: ${response.headers()}")
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.w("FCM", "Failed to subscribe to topics. Error: ${response.code()}")
                            Log.w("FCM", "Response error body: $errorBody")
                            Log.w("FCM", "Response headers: ${response.headers()}")
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("FCM", "Error subscribing to topics", e)
                    }
                }
            }
        }
    }
}
