package com.kk.zadaniekotlin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class MainViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val _isUserLoggedIn = MutableLiveData<Boolean>()
    val isUserLoggedIn: LiveData<Boolean> get() = _isUserLoggedIn

    private val _navigateToBasket = MutableLiveData<Boolean>()
    val navigateToBasket: LiveData<Boolean> get() = _navigateToBasket

    init {
        _isUserLoggedIn.value = FirebaseAuth.getInstance().currentUser != null
    }

    fun isUserAuthenticated(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun loginSuccess() {
        _isUserLoggedIn.value = true
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
        _isUserLoggedIn.value = false
    }

    fun triggerBasketNavigation() {
        _navigateToBasket.value = true
    }

    fun clearBasketNavigationFlag() {
        _navigateToBasket.value = false
    }
}
