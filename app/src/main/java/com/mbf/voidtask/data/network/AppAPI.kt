package com.lock.basesource.data.network

import com.lock.basesource.data.network.services.AppServives
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppAPI(private val networkConnectionInterceptor: NetworkConnectionInterceptor) {
    private val lockerRetrofit by lazy {
        val okkHttpclient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(AuthInterceptor(true))
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl("https://catfact.ninja/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideAppAPIService(): AppServives {
        return AppServives(lockerRetrofit)
    }
}