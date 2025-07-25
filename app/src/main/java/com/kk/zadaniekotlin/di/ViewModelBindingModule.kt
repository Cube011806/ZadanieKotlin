package com.kk.zadaniekotlin.di

import androidx.lifecycle.ViewModel
import com.kk.zadaniekotlin.MainViewModel
import com.kk.zadaniekotlin.SharedViewModel
import com.kk.zadaniekotlin.ui.basket.BasketViewModel
import com.kk.zadaniekotlin.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelBindingModule {

    @Binds
    @IntoMap
    @ViewModelKey(SharedViewModel::class)
    abstract fun bindSharedViewModel(viewModel: SharedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BasketViewModel::class)
    abstract fun bindBasketViewModel(viewModel: BasketViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}
