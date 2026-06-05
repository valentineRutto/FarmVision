package com.valentinerutto.farmvision.di

import com.valentinerutto.farmvision.MyApplication
import org.koin.dsl.module

val appModule = module {
    single { MyApplication.INSTANCE }


}