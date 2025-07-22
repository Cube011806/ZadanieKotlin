package com.kk.zadaniekotlin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val emailField = findViewById<EditText>(R.id.loginEmail)
        val passwordField = findViewById<EditText>(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val backButton = findViewById<Button>(R.id.backButton)

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.login(email, password)
            }
        }

        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Wypełnij wszystkie pola", Toast.LENGTH_SHORT).show()
            } else {
                loginViewModel.register(email, password)
            }
        }

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        loginViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> { /* Opcjonalny spinner */ }
                is AuthState.LoggedIn -> {
                    Toast.makeText(this, "Zalogowano pomyślnie!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                is AuthState.Registered -> {
                    Toast.makeText(this, "Zarejestrowano pomyślnie!", Toast.LENGTH_SHORT).show()
                }
                is AuthState.Error -> {
                    Toast.makeText(this, "Błąd: ${state.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
