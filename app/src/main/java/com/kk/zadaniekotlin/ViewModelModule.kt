package com.kk.zadaniekotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kk.zadaniekotlin.SharedViewModel
import com.kk.zadaniekotlin.ui.basket.BasketViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    fun provideSharedViewModel(): SharedViewModel = SharedViewModel()
    @Provides
    fun provideLoginViewModel(): LoginViewModel = LoginViewModel()

    @Provides
    fun provideBasketViewModel(): BasketViewModel = BasketViewModel()

    @Provides
    fun provideViewModelFactory(
        sharedViewModel: SharedViewModel,
        basketViewModel: BasketViewModel,
        loginViewModel: LoginViewModel
    ): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when (modelClass) {
                    SharedViewModel::class.java -> sharedViewModel as T
                    BasketViewModel::class.java -> basketViewModel as T
                    LoginViewModel::class.java -> loginViewModel as T
                    else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
                }
            }
        }
    }

}