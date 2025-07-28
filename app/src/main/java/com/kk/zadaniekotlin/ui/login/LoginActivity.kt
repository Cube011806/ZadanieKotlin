package com.kk.zadaniekotlin.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.kk.zadaniekotlin.MainActivity
import com.kk.zadaniekotlin.MyApplication
import com.kk.zadaniekotlin.R
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

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()
            loginViewModel.onLoginFormSubmitted(email, password)
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        emailField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                emailLayout.error = null
                loginViewModel.onEmailChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        passwordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                passwordLayout.error = null
                loginViewModel.onPasswordChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loginViewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> { }
                is AuthState.LoggedIn -> {
                    Toast.makeText(this, "Zalogowano pomyślnie!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
                is AuthState.Error -> {
                    Toast.makeText(this, "Błąd: ${state.message}", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        loginViewModel.formErrors.observe(this) { errors ->
            emailLayout.error = errors?.emailError
            passwordLayout.error = errors?.passwordError
        }
    }
}
