package com.example.admint2t

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONArray

class Inbox : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        val backButton = findViewById<FrameLayout>(R.id.backButton)
        val fullMessage =

        backButton.setOnClickListener {
            navigateToWelcomePage()
        }

        // Retrieve the stored messages from SharedPreferences
        val sharedPreferences = getSharedPreferences("InboxPrefs", Context.MODE_PRIVATE)
        val messagesJson = sharedPreferences.getString("messages", "[]") // Default to an empty JSON array

        // Parse the JSON array of messages
        val messagesArray = JSONArray(messagesJson)

        // Get the container LinearLayout where you want to display the messages
        val messagesContainer = findViewById<LinearLayout>(R.id.messageLinearLayout)

        // Inflate and populate a ConstraintLayout for each message
        val inflater = LayoutInflater.from(this)
        for (i in 0 until messagesArray.length()) {
            val messageObject = messagesArray.getJSONObject(i)
            val title = messageObject.getString("title")
            val recipientGroup = messageObject.getString("recipientGroup")
            val messageBody = messageObject.getString("messageBody")

            val timestamp: Long = if (messageObject.has("timestamp")) {
                messageObject.getLong("timestamp")
            } else {
                System.currentTimeMillis() // Use current time as a default for older messages
            }

            // Inflate the layout from the modified item_message layout resource
            val messageView = inflater.inflate(R.layout.item_message, messagesContainer, false) as ConstraintLayout

            // Populate the layout with the message details
            val messageTitle = messageView.findViewById<TextView>(R.id.subjectLine)
            val messageRecipientGroup = messageView.findViewById<TextView>(R.id.messageRecipientGroup)
            val messageBodyTextView = messageView.findViewById<TextView>(R.id.messageDescription)
            val messageTimestampTextView = messageView.findViewById<TextView>(R.id.timeAgoText)

            messageTitle.text = title
            messageRecipientGroup.text = recipientGroup
            messageBodyTextView.text = messageBody

            val elapsedTime = DateUtils.getRelativeTimeSpanString(timestamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)
            messageTimestampTextView.text = elapsedTime

            // Add the populated message layout to the container
            messagesContainer.addView(messageView)
        }
    }

    private fun navigateToWelcomePage() {
        val intent = Intent(this, AdminDashboard::class.java)
        startActivity(intent)
    }
}
