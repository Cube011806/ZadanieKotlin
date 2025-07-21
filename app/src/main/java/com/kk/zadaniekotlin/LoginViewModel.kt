package com.kk.zadaniekotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.LoggedIn
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "Nieznany błąd logowania")
            }
    }

    fun register(email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.Registered
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "Nieznany błąd rejestracji")
            }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object LoggedIn : AuthState()
    object Registered : AuthState()
    data class Error(val message: String) : AuthState()
}
