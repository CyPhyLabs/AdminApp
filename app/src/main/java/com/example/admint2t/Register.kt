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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.set
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Register : AppCompatActivity() {
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.RegisterFile)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load the animation
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Apply the animation to the root layout
        val rootLayout = findViewById<ConstraintLayout>(R.id.RegisterFile)
        rootLayout.startAnimation(fadeIn)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000") // Replace with your actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        authService = retrofit.create(AuthService::class.java)

        val firstNameEditText = findViewById<EditText>(R.id.firstNamelastNameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginTextView = findViewById<TextView>(R.id.loginTextView)

        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password == confirmPassword) {
                registerUser(firstName, email, password)
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

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
        loginTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun registerUser(firstName: String, email: String, password: String) {
        showLoadingScreen(true)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = authService.register(RegisterRequest(firstName, email, password, "staff"))
                withContext(Dispatchers.Main) {
                    showLoadingScreen(false)
                    clearForm()
                    Toast.makeText(this@Register, "Registration successful", Toast.LENGTH_SHORT).show()
                    navigateToLoginPage()
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    showLoadingScreen(false)
                    Toast.makeText(this@Register, "Registration failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun RegisterRequest(username: String, email: String, password: String): RegisterRequest {
        return RegisterRequest(username, email, password)
    }

    private fun clearForm() {
        findViewById<EditText>(R.id.firstNamelastNameEditText).text.clear()
        findViewById<EditText>(R.id.emailEditText).text.clear()
        findViewById<EditText>(R.id.passwordEditText).text.clear()
        findViewById<EditText>(R.id.confirmPasswordEditText).text.clear()
    }

    private fun showLoadingScreen(show: Boolean) {
        val loadingView = findViewById<View>(R.id.loadingView)
        loadingView.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToLoginPage() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}