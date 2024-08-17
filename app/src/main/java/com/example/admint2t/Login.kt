package com.example.admint2t

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgotPasswordTextView = findViewById<TextView>(R.id.forgotPasswordTextView)
        val backButton = findViewById<FrameLayout>(R.id.backButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            // Handle login logic here
            Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show()
        }

        forgotPasswordTextView.setOnClickListener {
            // Handle forgot password logic here
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            navigateToWelcomePage()
        }

        val loginTextView = findViewById<TextView>(R.id.registerNowTextView)
        val spannableString = SpannableString(loginTextView.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(this@Login, "Register Now has been pressed", Toast.LENGTH_SHORT).show()
            }
        }

        val start = loginTextView.text.indexOf("Register Now")
        val end = start + "Login Now".length
        spannableString[start, end] = clickableSpan

        loginTextView.text = spannableString
        loginTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()
    }

    private fun navigateToWelcomePage() {
        val intent = Intent(this, Welcome::class.java)
        startActivity(intent)
    }
}