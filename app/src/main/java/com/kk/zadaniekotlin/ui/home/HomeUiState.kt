package com.kk.zadaniekotlin.ui.home


sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Success(val imageUrls: List<String>) : HomeUiState()
    data object Empty : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
