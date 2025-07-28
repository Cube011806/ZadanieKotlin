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
import com.kk.zadaniekotlin.MyApplication
import com.kk.zadaniekotlin.R
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
            loginViewModel.onRegisterFormSubmitted(email, password, confirmPassword)
        }

        backButton.setOnClickListener {
            finish()
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

        confirmPasswordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                confirmLayout.error = null
                loginViewModel.onConfirmPasswordChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        loginViewModel.authState.observe(this) { state ->
            when (state) {
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

        loginViewModel.formErrors.observe(this) { errors ->
            emailLayout.error = errors?.emailError
            passwordLayout.error = errors?.passwordError
            confirmLayout.error = errors?.confirmPasswordError
        }
    }
}
