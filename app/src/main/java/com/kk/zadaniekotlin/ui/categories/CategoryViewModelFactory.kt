package com.kk.zadaniekotlin.ui.categories

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras

class CategoryViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val handle = extras.createSavedStateHandle()
        return CategoryViewModel(handle) as T
    }
}
