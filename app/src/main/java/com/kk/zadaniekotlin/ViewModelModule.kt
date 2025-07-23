package com.kk.zadaniekotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kk.zadaniekotlin.SharedViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun provideSharedViewModel(): SharedViewModel = SharedViewModel()

    @Provides
    fun provideViewModelFactory(
        sharedViewModel: SharedViewModel
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when (modelClass) {
                    SharedViewModel::class.java -> sharedViewModel as T
                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
