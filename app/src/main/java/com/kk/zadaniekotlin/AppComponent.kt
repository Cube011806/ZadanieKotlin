package com.kk.zadaniekotlin

import com.kk.zadaniekotlin.ui.home.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class])
interface AppComponent {
    fun inject(fragment: HomeFragment)
}

