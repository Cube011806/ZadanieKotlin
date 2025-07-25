package com.kk.zadaniekotlin

import android.app.Application
import com.kk.zadaniekotlin.di.AppComponent
import com.kk.zadaniekotlin.di.AppModule
import com.kk.zadaniekotlin.di.DaggerAppComponent

class MyApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}