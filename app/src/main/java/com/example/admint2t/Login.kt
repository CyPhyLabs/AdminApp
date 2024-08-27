package com.example.admint2t

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.set
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Load the animation
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Apply the animation to the root layout
        val rootLayout = findViewById<ConstraintLayout>(R.id.login)
        rootLayout.startAnimation(fadeIn)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000") // Replace with your actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authService = retrofit.create(AuthService::class.java)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val forgotPasswordTextView = findViewById<TextView>(R.id.forgotPasswordTextView)
        val backButton = findViewById<FrameLayout>(R.id.backButton)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginUser(email, password)
        }

        forgotPasswordTextView.setOnClickListener {
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
        }

        backButton.setOnClickListener {
            navigateToWelcomePage()
        }

        val loginTextView = findViewById<TextView>(R.id.registerNowTextView)
        val spannableString = SpannableString(loginTextView.text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@Login, Register::class.java)
                startActivity(intent)
            }
        }

        val start = loginTextView.text.indexOf("Register Now")
        val end = start + "Register Now".length
        spannableString[start, end] = clickableSpan

        loginTextView.text = spannableString
        loginTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun loginUser(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = authService.login(LoginRequest(email, password))
                saveTokens(response.access, response.refresh)

                withContext(Dispatchers.Main) {
                    navigateToDashboard()
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Login, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveTokens(accessToken: String, refreshToken: String) {
        val sharedPreferences = getSharedPreferences("auth", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("access_token", accessToken)
            putString("refresh_token", refreshToken)
            apply()
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this@Login, AdminDashboard::class.java)
        startActivity(intent)
    }

    private fun navigateToWelcomePage() {
        val intent = Intent(this, Welcome::class.java)
        startActivity(intent)
    }
}