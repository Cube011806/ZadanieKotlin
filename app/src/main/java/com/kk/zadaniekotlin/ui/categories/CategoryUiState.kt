package com.kk.zadaniekotlin.ui.categories

import com.kk.zadaniekotlin.model.Category

sealed class CategoryUiState {
    data object Loading : CategoryUiState()
    data class Success(val categories: List<Category>) : CategoryUiState()
    data object Empty : CategoryUiState()
    data class Error(val message: String) : CategoryUiState()
}
