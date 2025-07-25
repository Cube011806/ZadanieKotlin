package com.kk.zadaniekotlin.di

import com.kk.zadaniekotlin.MainActivity
import com.kk.zadaniekotlin.ui.basket.BasketFragment
import com.kk.zadaniekotlin.ui.categories.CategoryFragment
import com.kk.zadaniekotlin.ui.dashboard.DashboardFragment
import com.kk.zadaniekotlin.ui.home.HomeFragment
import com.kk.zadaniekotlin.ui.login.LoginActivity
import com.kk.zadaniekotlin.ui.login.RegisterActivity
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
        ViewModelBindingModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(fragment: HomeFragment)
    fun inject(fragment: CategoryFragment)
    fun inject(fragment: DashboardFragment)
    fun inject(fragment: BasketFragment)
    fun inject(activity: MainActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: RegisterActivity)


}