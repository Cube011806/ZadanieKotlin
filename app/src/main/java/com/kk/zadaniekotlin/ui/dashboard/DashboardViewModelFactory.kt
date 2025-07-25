package com.kk.zadaniekotlin.ui.dashboard

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras

class DashboardViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val handle = extras.createSavedStateHandle()
        return DashboardViewModel() as T
    }
}
