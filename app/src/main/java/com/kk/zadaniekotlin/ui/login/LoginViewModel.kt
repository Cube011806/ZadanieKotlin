package com.kk.zadaniekotlin.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = _authState

    private val _formErrors = MutableLiveData<FormErrors>()
    val formErrors: LiveData<FormErrors> get() = _formErrors

    fun onLoginFormSubmitted(email: String, password: String) {
        val isEmailValid = email.isNotBlank()
        val isPasswordValid = password.isNotBlank()

        if (!isEmailValid || !isPasswordValid) {
            _formErrors.value = FormErrors(
                emailError = if (!isEmailValid) "Email wymagany" else null,
                passwordError = if (!isPasswordValid) "Hasło wymagane" else null
            )
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.LoggedIn
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message ?: "Błąd logowania")
            }
    }

    fun onRegisterFormSubmitted(email: String, password: String, confirmPassword: String) {
        val isEmailValid = email.isNotBlank()
        val isPasswordValid = password.length >= 6
        val isPasswordMatch = password == confirmPassword

        if (!isEmailValid || !isPasswordValid || !isPasswordMatch) {
            _formErrors.value = FormErrors(
                emailError = if (!isEmailValid) "Email wymagany" else null,
                passwordError = if (!isPasswordValid) "Minimum 6 znaków" else null,
                confirmPasswordError = if (!isPasswordMatch) "Hasła nie są zgodne" else null
            )
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.Registered
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message ?: "Błąd rejestracji")
            }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object LoggedIn : AuthState()
    object Registered : AuthState()
    data class Error(val message: String) : AuthState()
}

data class FormErrors(
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)
