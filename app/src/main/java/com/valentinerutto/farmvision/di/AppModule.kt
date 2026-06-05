package com.valentinerutto.farmvision.di

import com.valentinerutto.farmvision.MyApplication
import com.valentinerutto.farmvision.data.local.FarmVisionDatabase
import com.valentinerutto.farmvision.data.network.ApiService
import com.valentinerutto.farmvision.util.RetrofitClient
import com.valentinerutto.farmvision.util.RetrofitClient.createOkClient
import org.koin.dsl.module
import retrofit2.Retrofit
import kotlin.coroutines.EmptyCoroutineContext.get

val appModule = module {
    single { MyApplication.INSTANCE }
}
    val networkingModule = module {
        single { RetrofitClient.provideOkHttpClient() }
        single { RetrofitClient.provideRetrofit(RetrofitClient.BASE_URL, get()) }

        single { createOkClient() }

        single {
            get<Retrofit>().create(ApiService::class.java)
        }
    }

val databaseModule ={
    single { FarmVisionDatabase.getDatabase(context = androidContext()) }
    single { get<FarmVisionDatabase>().FarmVisionDao() }

}


