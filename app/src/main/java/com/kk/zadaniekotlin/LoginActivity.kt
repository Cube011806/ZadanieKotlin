package com.kk.zadaniekotlin

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        val emailLayout = findViewById<TextInputLayout>(R.id.emailInputLayout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordInputLayout)
        val emailField = findViewById<EditText>(R.id.loginEmail)
        val passwordField = findViewById<EditText>(R.id.loginPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val backButton = findViewById<Button>(R.id.backButton)

       val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                emailLayout.error = if (emailField.text.isNullOrBlank()) "Email wymagany" else null
                passwordLayout.error = if (passwordField.text.isNullOrBlank()) "Hasło wymagane" else null
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        emailField.addTextChangedListener(watcher)
        passwordField.addTextChangedListener(watcher)

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()

            val isEmailValid = email.isNotEmpty()
            val isPasswordValid = password.isNotEmpty()

            emailLayout.error = if (!isEmailValid) getString(R.string.login_hintEmail) else null
            passwordLayout.error = if (!isPasswordValid) getString(R.string.login_hintPassword) else null

            if (isEmailValid && isPasswordValid) {
                loginViewModel.login(email, password)
           }
        }

        registerButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()

            val isEmailValid = email.isNotEmpty()
            val isPasswordValid = password.isNotEmpty()

            emailLayout.error = if (!isEmailValid) getString(R.string.login_hintEmail) else null
            passwordLayout.error = if (!isPasswordValid) getString(R.string.login_hintPassword) else null

            if (isEmailValid && isPasswordValid) {
                loginViewModel.register(email, password)
            }
        }

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        loginViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> { }
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
