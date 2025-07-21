package com.kk.zadaniekotlin.ui.basket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kk.zadaniekotlin.ui.categories.CategoryUiState
import javax.inject.Inject

class BasketViewModel(): ViewModel() {
    private val _uiState = MutableLiveData<BasketUiState>()
    val uiState: LiveData<BasketUiState> get() = _uiState
}