package com.example.admint2t

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.RegisterFile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            Toast.makeText(this, "Register has been pressed", Toast.LENGTH_SHORT).show()
        }


        val loginTextView = findViewById<TextView>(R.id.loginTextView)
        val spannableString = SpannableString(loginTextView.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@Register, "Login Now has been pressed", Toast.LENGTH_SHORT).show()
            }
        }

        val start = loginTextView.text.indexOf("Login Now")
        val end = start + "Login Now".length
        spannableString[start, end] = clickableSpan

        loginTextView.text = spannableString
        loginTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()

    }
}