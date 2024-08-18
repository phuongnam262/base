package com.lock.smartlocker.data.network

import com.lock.smartlocker.data.network.services.FaceServives
import com.lock.smartlocker.data.network.services.HardwareControlServices
import com.lock.smartlocker.data.network.services.LockerServives
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LockerAPI(private val networkConnectionInterceptor: NetworkConnectionInterceptor) {
    private val lockerRetrofit by lazy {
        val okkHttpclient = OkHttpClient.Builder()
            .addInterceptor(networkConnectionInterceptor)
            .addInterceptor(AuthInterceptor(true))
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
            .build()
        Retrofit.Builder()
            .client(okkHttpclient)
            .baseUrl("http://localhost:8532/")
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