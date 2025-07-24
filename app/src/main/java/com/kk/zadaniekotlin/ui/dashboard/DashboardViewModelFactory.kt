package com.kk.zadaniekotlin.ui.dashboard

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.kk.zadaniekotlin.ui.dashboardimport.DashboardViewModel

class DashboardViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val handle = extras.createSavedStateHandle()
        return DashboardViewModel(handle) as T
    }
}
