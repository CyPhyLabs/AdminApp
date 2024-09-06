package com.example.admint2t


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class Inbox : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        val backButton = findViewById<FrameLayout>(R.id.backButton)

        backButton.setOnClickListener {
            navigateToWelcomePage()
        }

        // Fetch the Bearer token from shared preferences
        val token = Login.getBearerToken(this)

        // Fetch notifications from API using NotificationService
        if (token != null) {
            fetchNotifications(token)
        } else {
            Toast.makeText(this, "No access token found. Please log in again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchNotifications(token: String) {
        // Call the fetchNotifications method from NotificationService
        NotificationService.fetchNotifications(token) // Adjust this line if necessary
    }

    private fun navigateToWelcomePage() {
        val intent = Intent(this, AdminDashboard::class.java)
        startActivity(intent)
    }
}
