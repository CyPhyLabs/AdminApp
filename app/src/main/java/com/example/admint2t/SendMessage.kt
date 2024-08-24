package com.example.admint2t
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.widget.FrameLayout
import org.json.JSONArray
import org.json.JSONObject
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SendMessage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sendmessage)

        val backButtontoDashboard = findViewById<FloatingActionButton>(R.id.backButtontoDashboard)
        backButtontoDashboard.setOnClickListener {
            navigatetodashboard()
        }

//just to show the dropdown list of recipients group
        val recipientsGroup = resources.getStringArray(R.array.recipientsgroup)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, recipientsGroup)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(adapter)

//logic behind the send message button
        val sendMessageButton = findViewById<Button>(R.id.sendMessageButton)
        sendMessageButton.setOnClickListener {
            val title = findViewById<EditText>(R.id.titleEditText).text.toString()
            val recipientGroup = autoCompleteTextView.text.toString()
            val messageBody = findViewById<EditText>(R.id.bodyMessageEditText).text.toString()
            //printing the values to check if the values are correct.
            println("Title: $title")
            println("Recipient Group: $recipientGroup")
            println("Message Body: $messageBody")

            sendMessageToBackend(title, recipientGroup, messageBody)
            saveMessageToInbox(title, recipientGroup, messageBody )
        }
    }

    private fun saveMessageToInbox(title: String, recipientGroup: String, messageBody: String) {
        val sharedPreferences = getSharedPreferences("InboxPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Retrieve the current messages from SharedPreferences
        val existingMessages = sharedPreferences.getString("messages", "[]")
        val messagesArray = JSONArray(existingMessages)

        // Create a new message object
        val newMessage = JSONObject()
        newMessage.put("title", title)
        newMessage.put("recipientGroup", recipientGroup)
        newMessage.put("messageBody", messageBody)
        newMessage.put("timestamp", System.currentTimeMillis() )

        // Add the new message to the array
        messagesArray.put(newMessage)

        // Save the updated array back to SharedPreferences
        editor.putString("messages", messagesArray.toString())
        editor.apply()
    }


    private fun sendMessageToBackend(title: String, recipientGroup: String, messageBody: String) {
        //Implement the API call to send data to the backend
    }


    private fun navigatetodashboard() {
        finish() // This function directly closes the activity, then goes to the dashboard.
    }
}
