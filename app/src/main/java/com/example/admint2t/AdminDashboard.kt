package com.example.admint2t

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        val newMessageButton = findViewById<FloatingActionButton>(R.id.newMessageButton)
        newMessageButton.setOnClickListener {
            val intent = Intent(this@AdminDashboard, SendMessage::class.java)
            startActivity(intent)
        }
    }
}