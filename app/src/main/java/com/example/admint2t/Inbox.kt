package com.example.admint2t

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.admint2t.APISetup.APIService
import com.example.admint2t.APISetup.Notification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Inbox : AppCompatActivity() {
    private val apiService = APIService()  // Use the APIService to handle notifications

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        val backButton = findViewById<FrameLayout>(R.id.backButton)
        backButton.setOnClickListener { finish() }

        val token = Login.getBearerToken(this)
        if (token != null) {
            // Calling the suspending function fetchNotifications
            CoroutineScope(Dispatchers.IO).launch {
                fetchNotifications(token)
            }
        }
    }

    // Convert fetchNotifications to a suspending function
    private suspend fun fetchNotifications(token: String) {
        try {
            // Fetch notifications using APIService
            val notifications = apiService.getNotifications(token)
            if (!notifications.isNullOrEmpty()) {
                // Call displayMessages within the coroutine to display notifications
                displayMessages(notifications)
            }
        } catch (e: Exception) {
            Log.e("API", "Error during API call", e)
            // Show error message in the UI thread
            showToastOnMain("An error occurred while fetching notifications.")
        }
    }

    // Launch a coroutine to call the suspending function
    private fun displayMessages(notifications: List<Notification>?) {
        CoroutineScope(Dispatchers.Main).launch {
            val messageContainer = findViewById<LinearLayout>(R.id.messageLinearLayout)
            val inflater = LayoutInflater.from(this@Inbox)

            notifications?.forEach { notification ->
                val messageView = inflater.inflate(R.layout.item_message, messageContainer, false)

                // Set notification data in the view
                val subjectLine = messageView.findViewById<TextView>(R.id.subjectLine)
                val messageDescription = messageView.findViewById<TextView>(R.id.messageDescription)

                subjectLine.text = notification.message.title
                messageDescription.text = notification.message.body

                // Add the view to the container
                messageContainer.addView(messageView)
            }
        }
    }

    // This is a helper function to show toast on the main thread (from any coroutine scope)
    private suspend fun showToastOnMain(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@Inbox, message, Toast.LENGTH_SHORT).show()
        }
    }
}
