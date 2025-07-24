package com.kk.zadaniekotlin

import com.kk.zadaniekotlin.ui.basket.BasketFragment
import com.kk.zadaniekotlin.ui.categories.CategoryFragment
import com.kk.zadaniekotlin.ui.dashboard.DashboardFragment
import com.kk.zadaniekotlin.ui.home.HomeFragment
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

