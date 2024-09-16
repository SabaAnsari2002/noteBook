package com.saba.notebook

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivityTheme10 : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main10)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Check if an image has been selected
        val selectedMainImage = sharedPreferences.getString("SELECTED_MAIN_IMAGE", null)
        if (selectedMainImage != null) {
            // Image is selected, proceed to MainActivityTheme9
            val intent = Intent(this, MainActivityTheme9::class.java)
            startActivity(intent)
            finish()
            return
        }

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            val userId = sharedPreferences.getInt("userId", -1)
            if (userId != -1) {
                val intent = Intent(this, HomeActivityTheme10::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            } else {
                // Redirect to login page if userId is not found
                startActivity(Intent(this, LoginActivityTheme10::class.java))
                finish()
            }
            return
        }

        val btnSignIn = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnSignIn.setOnClickListener {
            startActivity(Intent(this, RegisterActivityTheme10::class.java))
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivityTheme10::class.java))
        }
    }
}