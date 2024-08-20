package com.example.admint2t

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NewPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.create_new_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_new_password)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnNavigate = findViewById<LinearLayout>(R.id.btnVerify)
        btnNavigate.setOnClickListener {
            gotoNextPage()
        }
    }

    private fun gotoNextPage() {
        val intent = Intent(this, PasswordChangedSuccessActivity::class.java)
        startActivity(intent)
    }
}


class PasswordChangedSuccessActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_changed_success)
    }
}
