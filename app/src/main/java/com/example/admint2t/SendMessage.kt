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
import android.view.WindowInsetsAnimation
import android.widget.FrameLayout
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            val target_audience = autoCompleteTextView.text.toString()
            val body = findViewById<EditText>(R.id.bodyMessageEditText).text.toString()
            val token = Login.getBearerToken(this)
           //printing the values to check if the values are correct.
           println("Title: $title")
            println("Recipient Group: $target_audience")
           println("Message Body: $body")


            if (token != null) {
                sendMessageToBackend(token, title, target_audience, body)
            }
        }
    }

//    private fun saveMessageToInbox(title: String, recipientGroup: String, messageBody: String) {
//        val sharedPreferences = getSharedPreferences("InboxPrefs", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//
//        // Retrieve the current messages from SharedPreferences
//        val existingMessages = sharedPreferences.getString("messages", "[]")
//        val messagesArray = JSONArray(existingMessages)
//
//        // Create a new message object
//        val newMessage = JSONObject()
//        newMessage.put("title", title)
//        newMessage.put("recipientGroup", recipientGroup)
//        newMessage.put("messageBody", messageBody)
//        newMessage.put("timestamp", System.currentTimeMillis() )
//
//        // Add the new message to the array
//        messagesArray.put(newMessage)
//
//        // Save the updated array back to SharedPreferences
//        editor.putString("messages", messagesArray.toString())
//        editor.apply()
//    }


    private fun sendMessageToBackend(token: String, title: String, target_audience: String, body: String) {
        val message = SendMessageLogic.Message(title, target_audience, body)
        val authHeader = "Bearer $token"
        val call = RetrofitClient.instance.sendMessage(authHeader, message)

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@SendMessage, "Message sent successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SendMessage, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@SendMessage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun navigatetodashboard() {
        finish() // This function directly closes the activity, then goes to the dashboard.
    }
}