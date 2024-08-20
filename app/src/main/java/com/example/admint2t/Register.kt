package com.example.admint2t

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
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


        fun clearForm() {
            findViewById<EditText>(R.id.firstNamelastNameEditText).text.clear()
            findViewById<EditText>(R.id.emailEditText).text.clear()
            findViewById<EditText>(R.id.passwordEditText).text.clear()
            findViewById<EditText>(R.id.confirmPasswordEditText).text.clear()
        }

        fun showLoadingScreen(show: Boolean) {
            val loadingView = findViewById<View>(R.id.loadingView)
            loadingView.visibility = if (show) View.VISIBLE else View.GONE
        }



        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            // Simulate data saving with a delay
            Handler(Looper.getMainLooper()).postDelayed({
                clearForm()
                Toast.makeText(this, "Data Registered.", Toast.LENGTH_SHORT).show()
            }, 2000) // 2 seconds delay
        }


        val loginTextView = findViewById<TextView>(R.id.loginTextView)
        val spannableString = SpannableString(loginTextView.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@Register, Login::class.java)
                startActivity(intent)
            }
        }

        val start = loginTextView.text.indexOf("Login Now")
        val end = start + "Login Now".length
        spannableString[start, end] = clickableSpan

        loginTextView.text = spannableString
        loginTextView.movementMethod = android.text.method.LinkMovementMethod.getInstance()



    }
}