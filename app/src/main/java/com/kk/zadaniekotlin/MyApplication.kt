package com.kk.zadaniekotlin

import android.app.Application

class MyApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .build()
    }
}
