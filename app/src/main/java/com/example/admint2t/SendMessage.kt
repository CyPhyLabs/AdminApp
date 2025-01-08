package com.example.admint2t

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admint2t.APISetup.APIService
import com.example.admint2t.APISetup.Message
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendMessage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sendmessage)

        val backButtontoDashboard = findViewById<FloatingActionButton>(R.id.backButtontoDashboard)
        backButtontoDashboard.setOnClickListener {
            navigatetodashboard()
        }

        // Set up the dropdown list of recipients group
        val recipientsGroup = resources.getStringArray(R.array.recipientsgroup)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, recipientsGroup)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(adapter)

        // Logic for the send message button
        val sendMessageButton = findViewById<Button>(R.id.sendMessageButton)
        sendMessageButton.setOnClickListener {
            val title = findViewById<EditText>(R.id.titleEditText).text.toString()
            val targetAudience = autoCompleteTextView.text.toString()
            val body = findViewById<EditText>(R.id.bodyMessageEditText).text.toString()
            val token = Login.getBearerToken(this)

            // Print values for debugging
            println("Title: $title")
            println("Recipient Group: $targetAudience")
            println("Message Body: $body")

            if (token != null) {
                sendMessageToBackend(token, title, targetAudience, body)
            }
        }
    }

    private fun sendMessageToBackend(token: String, title: String, targetAudience: String, body: String) {
        val message = Message(title, body, target_audience = targetAudience)
        val apiService = APIService() // Create an instance of APIService

        // Call the sendMessage function from APIService
        CoroutineScope(Dispatchers.Main).launch {
            val success = apiService.sendMessage(token, message)
            if (success) {
                Toast.makeText(this@SendMessage, "Message sent successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@SendMessage, "Failed to send message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigatetodashboard() {
        finish() // Directly close the activity to navigate back to the dashboard.
    }
}
