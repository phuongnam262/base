package com.lock.smartlocker.data.network

import com.google.gson.GsonBuilder
import com.lock.smartlocker.data.models.LockerRetrieve
import com.lock.smartlocker.data.network.services.FaceServives
import com.lock.smartlocker.data.network.services.HardwareControlServices
import com.lock.smartlocker.data.network.services.LockerServives
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LockerAPI(private val networkConnectionInterceptor: NetworkConnectionInterceptor) {
    private val lockerRetrofit by lazy {
        val okkHttpclient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(AuthInterceptor(true))
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl("https://uatalamapisapp.smartlocker.vn/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val faceRetrofit by lazy {
        val okkHttpclient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(AuthInterceptor(false))
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl("http://localhost:8282/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val hwcRetrofit by lazy {
        val okkHttpclient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(AuthInterceptor(false))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl("http://14.161.16.191:8532/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideLockerAPIService(): LockerServives {
        return LockerServives(lockerRetrofit)
    }

    fun provideFaceAPIService(): FaceServives {
        return FaceServives(faceRetrofit)
    }

    fun provideHWCAPIService(): HardwareControlServices {
        return HardwareControlServices(hwcRetrofit)
    }
}