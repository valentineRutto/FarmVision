package com.valentinerutto.rainintel

import android.app.Application
import android.util.Log
import com.valentinerutto.rainintel.data.local.CitySeeder
import com.valentinerutto.rainintel.di.appModule
import com.valentinerutto.rainintel.di.databaseModule
import com.valentinerutto.rainintel.di.networkingModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.ext.android.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class MyApplication: Application() {
    private val citySeeder: CitySeeder by inject()
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        lateinit var INSTANCE: MyApplication
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        startKoin{
            androidLogger( level = Level.DEBUG)
            androidContext(this@MyApplication)

            modules(appModule,networkingModule, databaseModule)


        }

        applicationScope.launch {
            runCatching {
                citySeeder.seedIfNeeded()
            }.onFailure { throwable ->
                Log.e("MyApplication", "Unable to seed cities", throwable)
            }
        }
    }
}
