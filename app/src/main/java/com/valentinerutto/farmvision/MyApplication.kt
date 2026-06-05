package com.valentinerutto.farmvision

import android.app.Application
import com.valentinerutto.farmvision.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    companion object {
        lateinit var INSTANCE: MyApplication
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        startKoin{
            androidLogger( level = Level.DEBUG)
            androidContext(this@MyApplication)
            modules(appModule)
        }

    }
}