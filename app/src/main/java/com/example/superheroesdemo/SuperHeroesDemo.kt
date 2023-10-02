package com.example.superheroesdemo

import android.app.Application
import com.example.superheroesdemo.cdi.domainModule
import com.example.superheroesdemo.cdi.remoteModule
import com.example.superheroesdemo.cdi.repositoryModule
import com.example.superheroesdemo.cdi.viewModelModule
import localModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SuperHeroesDemo: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SuperHeroesDemo)
            KoinLogger()
            modules(viewModelModule, repositoryModule, localModule, remoteModule, domainModule)
        }
    }
}