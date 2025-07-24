package com.kk.zadaniekotlin

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class RegisterActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        val emailLayout = findViewById<TextInputLayout>(R.id.registerEmailLayout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)
        val confirmLayout = findViewById<TextInputLayout>(R.id.passwordConfirmLayout)

        val emailField = findViewById<EditText>(R.id.registerEmail)
        val passwordField = findViewById<EditText>(R.id.registerPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.confirmPassword)

        val registerButton = findViewById<Button>(R.id.registerButton)
        val backButton = findViewById<Button>(R.id.backButton)

        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            val isEmailValid = email.isNotEmpty()
            val isPasswordValid = password.length >= 6
            val isPasswordMatch = password == confirmPassword

            emailLayout.error = if (!isEmailValid) "Email wymagany" else null
            passwordLayout.error = when {
                !isPasswordValid -> "Minimum 6 znaków"
                else -> null
            }
            confirmLayout.error = if (!isPasswordMatch) "Hasła nie są zgodne"
            else if (!isPasswordValid) "Minimum 6 znaków" else null

            if (isEmailValid && isPasswordValid && isPasswordMatch) {
                loginViewModel.register(email, password)
            }
        }

        backButton.setOnClickListener {
            finish()
        }

        loginViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> { }
                is AuthState.Registered -> {
                    Toast.makeText(this, "Zarejestrowano pomyślnie!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is AuthState.Error -> {
                    Toast.makeText(this, "Błąd: ${state.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}
