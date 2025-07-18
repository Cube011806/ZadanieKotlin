package com.kk.zadaniekotlin.ui.basket

import com.kk.zadaniekotlin.model.Basket

sealed class BasketUiState {
    data object Loading : BasketUiState()
    data class Success(val basket: List<Basket>) : BasketUiState()
    data object Empty : BasketUiState()
    data class Error(val message: String) : BasketUiState()
}
